package com.naiyarv;

import com.naiyarv.dictionary.DictionaryReader;
import com.naiyarv.factory.PhoneNumbersProcessorFactory;
import com.naiyarv.processor.PhoneNumberProcessor;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This is the main class for Phone number matcher process
 */
public class Main {

    /**
     * Main method for Phone Numbers Matcher process
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting Phone Numbers Matcher process...");

        // Parse the aruments
        ArgsParser argsParser = new ArgsParser();
        argsParser.parseArgs(args);

        String dictionaryFilePath = argsParser.getDictionaryFilePath();
        Collection<String> phoneNumbersFilePaths = argsParser.getPhoneNumbersFilePaths();

        // Load dictionary
        DictionaryReader dictionaryReader = new DictionaryReader(dictionaryFilePath);
        dictionaryReader.readDictionaryFile(); // read dictonary and populate
        Map<String, Set<String>> dictionary = dictionaryReader.getDictionary();

        // Fetch phone number processor
        PhoneNumberProcessor phoneNumberProcessor = PhoneNumbersProcessorFactory.getInstance().createPhoneNumberProcessor(phoneNumbersFilePaths, dictionary);
        phoneNumberProcessor.processPhoneNumbers();  // Process phone numbers

        System.out.println("Stopping Phone Numbers Matcher process...");
    }


}
