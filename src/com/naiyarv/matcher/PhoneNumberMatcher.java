package com.naiyarv.matcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.naiyarv.constants.StringConstants.EMPTY_STRING;
import static com.naiyarv.constants.StringConstants.WORD_BOUNDARY_CHARACTER;


/**
 * Class which has the logic to print all combination of words
 * for a phone number based on the words
 * available in dictionary
 *
 * Created by vikasnaiyar
 */
public class PhoneNumberMatcher implements Matcher {

    private Map<String, Set<String>> dictionary = null;

    public PhoneNumberMatcher(Map<String, Set<String>> dictionary) {
        if (dictionary == null || dictionary.isEmpty()) {
            throw new IllegalArgumentException("Dictionary object cannot be null or empty");
        }

        this.dictionary = dictionary;
    }

    @Override
    public void printAllMatches(String phoneNumber) {
        Collection<String> matchingWords = findMatchingWords(phoneNumber);

        if (matchingWords != null && !matchingWords.isEmpty()) {
            System.out.format("Printing matching numbers for phone number %s %n", phoneNumber);
            matchingWords.stream().forEach(System.out::println);
        } else {
            System.out.format("No match found for number %s %n", phoneNumber);
        }
    }

    /**
     * Recursive approach to find all words combination which can be used
     * to represent a phone number.
     *
     * Logic:-
     *     Starting from the first digit of the phone number
     *     lookup in dictionary for that digit.
     *
     *     Will will keep looking for key in dictionary which match combination of
     *     digits (starting from zeroth index to subsequent index)
     *
     *     If the key == digit (or combination of digits) is found in the dictionary
     *     then corresponding word(s) will be selected for printing.
     *
     *     If no key is found in the dictionary from the start of digit to the end of the
     *     phone number then then we will skip that digit.
     *
     *     If two consecutive numbers are skipped, we will break; hence nothing will be printed for that phone number.
     *
     *    P.S:- Making this method public only for the sake of testing
     * @param phoneNumber
     * @return
     */
     public Collection<String> findMatchingWords(String phoneNumber) {
        Collection<String> matchingWords = new HashSet<>();
        int wordCount = phoneNumber.length();
        String digitWordPrefix = EMPTY_STRING;
        boolean matchFound = false;

        for (int startIndex = 0; startIndex < wordCount; startIndex++) {
            // If match has been found with start digit at zeroth index
            // Or more than two digits has been skipped (no match found starting from digits at index 0 & 1)
            // Then we break and print no numbers
            if(matchFound || startIndex > 1) {
                break;
            } else if(startIndex == 1) { // If only zeroth index digit is skipped, it will form the prefix
                digitWordPrefix = phoneNumber.charAt(0) + WORD_BOUNDARY_CHARACTER;
            }

            /**
             * Start find the matching prefix and suffix words
             */
            for (int endIndex = startIndex + 1; endIndex <= wordCount; endIndex++) {
                String processedNumber = phoneNumber.substring(startIndex, endIndex);
                // If dictionary contains this number
                if (dictionary.containsKey(processedNumber)) {
                    // Find the prefix words
                    Set<String> prefixWords = dictionary.get(processedNumber);

                    if (prefixWords != null && !prefixWords.isEmpty()) {
                        if (endIndex == wordCount) { // all digits have been processed
                            matchFound = true;
                            for (String prefixWord : prefixWords) {
                                matchingWords.add(digitWordPrefix + prefixWord);
                            }
                        } else { // few digits are remaining
                            String unProcessedNumber = phoneNumber.substring(endIndex);
                            Collection<String> suffixWords = findMatchingWords(unProcessedNumber);

                            if (suffixWords != null && !suffixWords.isEmpty()) { // check if suffix words are available
                                matchFound = true;
                                for (String prefixWord : prefixWords) {
                                    for (String suffixWord : suffixWords) {
                                        matchingWords.add(digitWordPrefix + prefixWord + WORD_BOUNDARY_CHARACTER + suffixWord);
                                    }
                                }
                            } else {
                                if (unProcessedNumber.length() == 1) { // last digit left unprocessed
                                    matchFound = true;
                                    for (String prefixWord : prefixWords) {
                                        matchingWords.add(digitWordPrefix + prefixWord + WORD_BOUNDARY_CHARACTER + unProcessedNumber);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        return matchingWords;
    }

}
