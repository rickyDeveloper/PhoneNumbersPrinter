package com.naiyarv.processor;

import com.naiyarv.matcher.Matcher;
import static com.naiyarv.constants.StringConstants.PUNCTUATION_SPACE_REGEX_STRING;
import static com.naiyarv.constants.StringConstants.WHITE_SPACE_REGEX_STRING;
import static com.naiyarv.constants.StringConstants.EMPTY_STRING;

/**
 *
 * Abstract class for phone number processor
 * Created by vikasnaiyar
 *
 */
public abstract class PhoneNumberProcessor {

    // Holds the interface of matcher as we may have multiple types of matchers
    private Matcher matcher;

    public PhoneNumberProcessor(Matcher matcher){
        this.matcher = matcher;
    }

    // This needs to implmeneted by sub classes
    public abstract void processPhoneNumbers();

    /**
     * Printing all the possible matches
     * @param phoneNumber
     */
    protected void printAllMatchesForPhoneNumber(String phoneNumber) {
        phoneNumber = removePunctuationCharacters(phoneNumber);
        phoneNumber = removeWhiteSpaces(phoneNumber);
        this.matcher.printAllMatches(phoneNumber);
    }

    /**
     * Remove all punctuation
     * @param phoneNumber
     * @return
     */
    private String removePunctuationCharacters(String phoneNumber){
        return phoneNumber.replaceAll( PUNCTUATION_SPACE_REGEX_STRING, EMPTY_STRING );
    }

    /**
     * Remove all white spaces
     * @param phoneNumber
     * @return
     */
    private String removeWhiteSpaces(String phoneNumber){
        return phoneNumber.replaceAll( WHITE_SPACE_REGEX_STRING, EMPTY_STRING );
    }
}
