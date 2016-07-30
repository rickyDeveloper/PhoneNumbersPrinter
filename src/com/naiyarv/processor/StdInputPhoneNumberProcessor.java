package com.naiyarv.processor;

import com.naiyarv.matcher.Matcher;

import java.util.Scanner;

import static com.naiyarv.constants.StringConstants.EXIT_STRING;

/**
 * Input phone numbers from STDIN
 * and process them.
 * <p>
 * When the user type's  'exit' the processor will stop
 * taking input for phone numbers.
 * <p>
 * Created by vikasnaiyar
 */
public class StdInputPhoneNumberProcessor extends PhoneNumberProcessor {

    private Scanner scanner = null;

    public StdInputPhoneNumberProcessor(Scanner scanner, Matcher matcher) {
        super(matcher);
        this.scanner = scanner;
    }

    @Override
    public void processPhoneNumbers() {
        System.out.println("Please enter phone numbers:");
        System.out.println("P.S:- When all phone numbers have been enterered, pls type \'" + EXIT_STRING + "\'");
        String phoneNumber = null;
        while ((phoneNumber = scanner.nextLine()) != null) {
            if (EXIT_STRING.equalsIgnoreCase(phoneNumber.trim())) {
                break;
            } else {
                printAllMatchesForPhoneNumber(phoneNumber);
            }
        }

    }

}
