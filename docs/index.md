# Developer documentation

This site hosts **public documentation** for projects that are otherwise developed in private repositories.

## Agentic Customer Due Diligence (CDD)

Reference docs for an agent-orchestrated CDD/KYC workflow built with the **AWS Strands Agents SDK** and **LangGraph**, with mock data and deterministic compliance tools.

**Start here:** [Agentic CDD with AWS Strands](cdd/AGENTIC_CDD_WITH_AWS_STRANDS.md)

| Guide | Description |
| --- | --- |
| [CDD example overview](cdd/README.md) | Workflow diagram, layout, run commands, Strands vs LangGraph |
| [Agentic CDD with AWS Strands](cdd/AGENTIC_CDD_WITH_AWS_STRANDS.md) | Full developer guide — patterns, templates, runbook |
| [AWS Strands architecture](cdd/AWS_STRANDS_CDD.md) | Single-agent + one-tool Strands wiring |
| [Template manager](cdd/TEMPLATE_MANAGER.md) | Markdown macro/sub-workflow orchestration |
| [Graph RFI routing spec](cdd/GRAPH_RFI_ROUTING_SPEC.md) | GraphBuilder edges for RFI / rework loops |

!!! note "Runnable code"
    The implementation lives in a **private** repo (`agentic-ops`, path `cdd_example/`). These pages describe design and usage; clone that repo to run the examples locally.

## PhoneNumbersPrinter

The original project in this repository is a Java utility that maps phone digits to dictionary words. See the [repository README on GitHub](https://github.com/rickyDeveloper/PhoneNumbersPrinter#phonenumbersprinter).
