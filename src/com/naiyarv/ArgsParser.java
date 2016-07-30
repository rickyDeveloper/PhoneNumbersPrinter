package com.naiyarv;

import java.util.HashSet;
import java.util.Collection;

import static com.naiyarv.constants.StringConstants.PHONE_NUMBERS_DICTIONARY_ARG;
import static com.naiyarv.constants.StringConstants.DEFAULT_PHONE_NUMBERS_DICTIONARY_FILE_PATH;

/**
 * Created by vikasnaiyar
 * <p>
 * Concrete class to parse arguments for the program.
 * <p>
 * If few arguments are not passed then this class sets the default value of that argument.
 */
public class ArgsParser {

    private Collection<String> phoneNumbersFilePaths;

    private String dictionaryFilePath = "";

    public ArgsParser() {
        phoneNumbersFilePaths = new HashSet<>();
    }

    /**
     * Parser of args
     *
     * @param args
     */
    public void parseArgs(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("No program args passed");
        } else {
            System.out.println("Parsing program args");
            int argsCount = args.length;
            for (int index = 0; index < argsCount; ) {
                String arg = args[index];
                // Process only non-empty arguments
                if (arg != null && !arg.trim().isEmpty()) {
                    if (PHONE_NUMBERS_DICTIONARY_ARG.equalsIgnoreCase(args[index])) {
                        if (++index < argsCount) {
                            dictionaryFilePath = args[index];
                        }
                    } else {
                        phoneNumbersFilePaths.add(args[index]);
                    }
                }

                ++index;
            }
        }
    }

    public String getDictionaryFilePath() {
        // If dictionary file path is not passed then
        if (dictionaryFilePath == null || dictionaryFilePath.trim().isEmpty()) {
            dictionaryFilePath = DEFAULT_PHONE_NUMBERS_DICTIONARY_FILE_PATH;
            System.out.format("Using default dictionary path %s%n", dictionaryFilePath);
        } else {
            System.out.format("Using dictionary path %s%n", dictionaryFilePath);
        }

        return dictionaryFilePath;
    }


    public Collection<String> getPhoneNumbersFilePaths() {
        return phoneNumbersFilePaths;
    }

}
