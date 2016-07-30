package com.naiyarv.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import com.naiyarv.matcher.Matcher;

/**
 * This is Files phone number processor
 * Created by vikasnaiyar
 */
public class FilesPhoneNumberProcessor extends PhoneNumberProcessor {
    // Collection of all the file paths
    private Collection<String> filePaths = null;

    public FilesPhoneNumberProcessor(Collection<String> filePaths, Matcher matcher) {
        super(matcher);
        if(filePaths == null || filePaths.isEmpty()) {
            throw new IllegalArgumentException("File paths cannot be null or empty");
        }
        this.filePaths = filePaths;
    }


    @Override
    public void processPhoneNumbers() {
        // Read each file and then print all matching phone numbers
        filePaths.forEach(filePath -> {
            System.out.println("Reading phone numbers from file --> " + filePath.trim());
            try {
                Files.lines(new File(filePath.trim()).toPath())
                        .forEach(phoneNumber -> {
                            printAllMatchesForPhoneNumber(phoneNumber);
                        });
            } catch (IOException io) {
                System.err.format("IOException occured while reading phone file %s, %s%n", filePath, io.getMessage());
                throw new RuntimeException("IOException occured while reading phone file =" +filePath);
            }
        });
    }

}
