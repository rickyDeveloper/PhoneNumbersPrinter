# GraphBuilder wiring: statuses, `invocation_state`, and redo edges

This maps **summarisation → KYC → transaction review → RFI**, with **RFI-triggered rework** back toward **KYC** (and optional paths), onto **AWS Strands `GraphBuilder`** patterns and **`invocation_state`** keys.

Strands **`condition` predicates on edges receive `GraphState`**, not your app dict directly. Two robust patterns:

1. **Thin router node after each branching point** (`MultiAgentBase` / function executor) reads `invocation_state`, writes **`ROUTING_MARKER=...`** into an `AgentResult`-shaped blob that shows up under `GraphState.results[router_node_id].result` as text — predicates parse that string (**same trick as legacy `RISK_BAND=` in examples**).

2. **Or** stash nothing in prose: **router writes** `ROUTING_MARKER` only; all **real data** stays in `invocation_state` below — edges only peek at markers.

Below we use **`ROUTING_MARKER=...`** on **router exits** plus **`invocation_state["case"]["workflow"]`** for audit and human-readable truth.

---

## 1. `invocation_state` shape (recommended keys)

Treat **`invocation_state`** as passed into every **`Agent`/node** invocation (Strands docs: shared state).

```yaml
invocation_state:
  application_id: "<string>"          # idempotency, logging
  correlation_id: "<string>"

  case:
    # Normalised dossier fed to every reviewer
    normalized_profile: {}          # from early ACIP/normalisation subgraph
    party_graph_ref: {}             # optional: link to party graph / snapshots

    # Lane outputs — append-only or version per round
    reviews:
      summarisation:
        round: <int>
        summary_text: ""
        artifacts: []
        completed_at: ""              # iso8601 optional
      kyc:
        round: <int>
        status: "<pass|fail|needs_info|defer>"
        rationale: ""
        exceptions: []
        completed_at: ""
      transactions:
        round: <int>
        status: "<pass|fail|needs_info|elevated_risk>"
        findings: []
        completed_at: ""

    rfi:
      active: <bool>
      rounds: <int>                  # how many formal RFI cycles
      triggers: ["<code>", ...]     # why we opened/sustain RFI
      open_items:
        - id: "<uuid>"
          question: ""
          blocking: <bool>
          satisfied: <bool>
      last_request_at: ""
      deadline_at: ""               # policy / SLA

    # Explicit routing intents set by deterministic policy or reviewer tools — router reads THIS
    workflow:
      next_gate: "<string>"           # authoritative for router emitted marker (see §4)
      blockers: []
      escalation_reason: ""

    redo:
      redo_kyc: <bool>
      redo_summarisation: <bool>
      redo_transactions: <bool>
      rationale: ""

  telemetry:
    routing_history: []
```

**Agents/tools** mutate **`invocation_state["case"]["reviews"][...]`** and **`workflow` / `rfi` / `redo`**; only the **router** sets **`ROUTING_MARKER`** for graph edges.

---

## 2. Status vocabulary (minimal)

Use **narrow enums** everywhere so predicates stay testable:

| Scope | Status / codes | Meaning |
|-------|-----------------|--------|
| **KYC** | `pass` | Proceed default path |
| | `fail` | Hard stop / escalate (edge to human or reject subgraph) |
| | `needs_info` | Prefer **RFI** or internal data pull |
| | `defer` | Wait external source (timeline node, not modeled here) |
| **Txn** | `pass` / `fail` / `needs_info` / `elevated_risk` | Last two often funnel to RFI or KYC rethink |
| **RFI** | `active=true` while items unsatisfied |
| **`workflow.next_gate`** (router source of truth before marker) | `CONTINUE_AFTER_SUMMARY` … | Internal; §4 |

**RFI trigger codes** (`case.rfi.triggers`): e.g. `KYC_NEEDS_INFO`, `TXN_OUTLIER`, `ADDRESS_MISMATCH`, `PEP_DOCUMENTATION_GAP`, **policy codes you own.**

---

## 3. Nodes (logical GraphBuilder IDs)

Fixed **sequential spine** unless you refactor:

| `node_id` | Executor typical | Responsibility |
|-----------|-------------------|----------------|
| `entry_acip` | function / subgraph | Load case, normalization, deterministic ACIP checklist — sets `normalized_profile` |
| `summarisation` | `Agent` | Writes `case.reviews.summarisation` |
| `kyc_review` | `Agent` | Writes `case.reviews.kyc`, may set redo flags |
| `txn_review` | `Agent` | Writes `case.reviews.transactions` |
| `rfi_prep` | `Agent` **or** HITL | Opens/updates `case.rfi`, `open_items` |
| **`router_post_rfi`** | **deterministic only** | Reads `redo`/`workflow`; emits **`ROUTING_MARKER=...`** (§4) |
| `route_after_summ` | deterministic | Rarely needed if always `summ→kyc` unconditional |
| `human_escalation` | HITL / notify | Blocking failures |
| `complete` | function | Persist case closure |

Optional **between-KYC loops**: add **`router_post_kyc`**, **`router_post_txn`** if you branch before RFI (not shown in minimal list below).

---

## 4. `ROUTING_MARKER` values emitted by **`router_post_rfi`**

Map **`case.workflow.next_gate`** (your code sets this in RFI closeout / policy tooling) → **exact string substring** predicates use:

| `ROUTING_MARKER` | When |
|------------------|------|
| `LOOP_KYC` | `redo.redo_kyc` **or** RFI closure requires KYC rework without full resummarisation |
| `LOOP_SUMMARISATION` | New material substantive enough to rerun summary (documents replaced, party change) |
| `LOOP_TRANSACTIONS` | Only txn slice must rerun (depends on upstream policy) |
| `CONTINUE_RFI` | RFI still **`active`** with blocking items |
| `ESCALATE` | Compliance blockers / `fail` semantics |
| `COMPLETE` | All gates cleared, no open blocking RFI |

**Pseudo for `router_post_rfi`:**

```python
def route_post_rfi(graph_state_placeholder, invocation_state):  # your real executor reads invocation_state only
    c = invocation_state["case"]["workflow"]["next_gate"]  # you maintain this alongside agents
    redo = invocation_state["case"]["redo"]
    rfi = invocation_state["case"]["rfi"]
    # Policy priority example:
    if rfi["active"] and any(i["blocking"] and not i["satisfied"] for i in rfi["open_items"]):
        marker = "ROUTING_MARKER=CONTINUE_RFI"
    elif redo["redo_summarisation"]:
        marker = "ROUTING_MARKER=LOOP_SUMMARISATION"
    elif redo["redo_kyc"]:
        marker = "ROUTING_MARKER=LOOP_KYC"
    elif redo.get("redo_transactions"):
        marker = "ROUTING_MARKER=LOOP_TRANSACTIONS"
    elif c == "COMPLETE":
        marker = "ROUTING_MARKER=COMPLETE"
    else:
        marker = "ROUTING_MARKER=ESCALATE"
    # Return AgentResult with message text = marker
```

---

## 5. Concrete `GraphBuilder` edges (core loop)

Assume **`entry_acip` → summarisation → …` already wired**.

```python
builder.add_edge("entry_acip", "summarisation")

# Primary forward spine:
builder.add_edge("summarisation", "kyc_review")
builder.add_edge("kyc_review", "txn_review")
builder.add_edge("txn_review", "rfi_prep")

# Exit RFI into router (always):
builder.add_edge("rfi_prep", "router_post_rfi")

# Loopbacks (predicates inspect str(GraphState.results["router_post_rfi"].result)):
builder.add_edge("router_post_rfi", "summarisation", condition=lambda s: "LOOP_SUMMARISATION" in str(s.results["router_post_rfi"].result))
builder.add_edge("router_post_rfi", "kyc_review", condition=lambda s: "LOOP_KYC" in str(s.results["router_post_rfi"].result))
builder.add_edge("router_post_rfi", "txn_review", condition=lambda s: "LOOP_TRANSACTIONS" in str(s.results["router_post_rfi"].result))
builder.add_edge("router_post_rfi", "rfi_prep", condition=lambda s: "CONTINUE_RFI" in str(s.results["router_post_rfi"].result))

builder.add_edge("router_post_rfi", "human_escalation", condition=lambda s: "ESCALATE" in str(s.results["router_post_rfi"].result))
builder.add_edge("router_post_rfi", "complete", condition=lambda s: "COMPLETE" in str(s.results["router_post_rfi"].result))

builder.set_max_node_executions(50)       # cyclic safety
builder.set_execution_timeout(...)
```

**Note:** Multiple edges from **`router_post_rfi`** imply **exclusive** branching — enforce **exactly one** true condition in **`router_post_rfi`** logic (policy order above).

---

## 6. RFI redo rules expressed as **`invocation_state` updates**

| Trigger | Typical writer | **`redo` / `workflow` suggestion** |
|--------|-----------------|-------------------------------------|
| KYC reviewer sets `needs_info` | `kyc_review` tool outcome | `rfi.active=true`, push `RFI_PREP`, **later** closing RFI with new POI → **`redo_redo_kyc=true`** via policy |
| Txn anomalies | `txn_review` | `elevated_risk` + RFI docs for source of wealth → **`redo_transactions`** optional after new docs |
| Customer supplied new proofs closing RFI | `rfi_prep` / back-office API | **`redo_redo_kyc=true`** only if proofs affect identity; **`redo_summarisation=true`** if dossier materially changed |

**Bump `reviews.<lane>.round` on every re-entry** to that **`Agent`** for audit versioning.

---

## 7. What “stitch between agents” means here

| Mechanism | Role |
|-----------|------|
| **`invocation_state["case"]`** | Long-lived dossier — **canonical** payloads between agents |
| **Graph dependency input** | Strands forwards predecessor **text/results** automatically — supplement, don’t duplicate |
| **`ROUTING_MARKER`** | **Edge guard** bridging **app state** ↔ **`GraphState` predicates**

---

## 8. Checklist before production

- [ ] One **exclusive** resolver order in **`router_post_rfi`**
- [ ] **Max executions** / timeout on cyclic graph
- [ ] **Instrumentation**: append to **`telemetry.routing_history`** on each routing decision
- [ ] **Separate** subgraph for **heavy ACIP checklist** feeding **`normalized_profile`** before **`summarisation`**

---

This file is illustrative — align marker strings and enums with your **legal / operational** playbook and **SDK version**.
