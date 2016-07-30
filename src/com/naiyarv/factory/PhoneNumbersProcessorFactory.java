package com.naiyarv.factory;

import com.naiyarv.matcher.Matcher;
import com.naiyarv.matcher.PhoneNumberMatcher;
import com.naiyarv.processor.FilesPhoneNumberProcessor;
import com.naiyarv.processor.PhoneNumberProcessor;
import com.naiyarv.processor.StdInputPhoneNumberProcessor;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Factory class for creating phone number processor objects
 * Created by vikasnaiyar
 */
public class PhoneNumbersProcessorFactory {

    private static final PhoneNumbersProcessorFactory INSTANCE = new PhoneNumbersProcessorFactory();

    // Need this to make this class singleton
    private PhoneNumbersProcessorFactory() {

    }

    public static PhoneNumbersProcessorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Creational pattern
     * @param phoneNumbersFilePaths
     * @param dictionary
     * @return
     */
    public PhoneNumberProcessor createPhoneNumberProcessor(Collection<String> phoneNumbersFilePaths, Map<String, Set<String>> dictionary) {
        PhoneNumberProcessor phoneNumberProcessor = null;
        Matcher matcher = new PhoneNumberMatcher(dictionary);

        if(phoneNumbersFilePaths == null || phoneNumbersFilePaths.isEmpty()) {
            phoneNumberProcessor = new StdInputPhoneNumberProcessor(new Scanner(System.in), matcher);
        } else {
            phoneNumberProcessor = new FilesPhoneNumberProcessor(phoneNumbersFilePaths, matcher);
        }

        return phoneNumberProcessor;
    }

}
