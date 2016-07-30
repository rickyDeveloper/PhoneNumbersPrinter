package com.naiyarv.dictionary;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import static com.naiyarv.constants.StringConstants.ALPHABETS_ONLY_REGEX_STRING;
import com.naiyarv.enums.NumberEncodingEnum;


/**
 * Concrete class for reading dictionary
 * Created by vikasnaiyar
 */
public class DictionaryReader {

    private String dictionaryFilePath;

    private Map<String, Set<String>> dictionary;

    public DictionaryReader(String dictionaryFilePath) {
        this.dictionaryFilePath = dictionaryFilePath;
        dictionary = new HashMap<>();
    }

    /**
     * Read the dictionary file
     */
    public void readDictionaryFile() {
        System.out.format("Loading dictionary...%n");
        StringBuilder numberBuilder = new StringBuilder();
        try {
            Files.lines(new File(dictionaryFilePath.trim()).toPath())
                    .map(word -> word.replaceAll(ALPHABETS_ONLY_REGEX_STRING, "").toUpperCase())
                    .filter(word -> !word.isEmpty())
                    .forEach(word -> {
                        numberBuilder.setLength(0);
                            word.chars()
                                .mapToObj(ch -> (char)ch)
                                .forEach( ch -> {
                                    for (NumberEncodingEnum encondingEnum: NumberEncodingEnum.values()) {
                                        if(encondingEnum.getCharacters().contains(ch.toString())) {
                                            numberBuilder.append(encondingEnum.getDigit());
                                            break;
                                        }
                                    }
                                });
                        String number = numberBuilder.toString();
                        if(dictionary.containsKey(number)) {
                           dictionary.get(number).add(word);
                        } else {
                            Set<String> wordSet = new HashSet<>();
                            wordSet.add(word);
                            dictionary.put(numberBuilder.toString(), wordSet);
                        }
                     });
        } catch (IOException io) {
            System.err.format("IOException occured while reading dictionary file %s%n", io.getMessage());
            throw new RuntimeException("IOException occured while reading dictionary file =" +dictionaryFilePath);
        }

        // Printing this only for info purpose of end user
        dictionary.forEach((k,v) -> System.out.format("Dictionary key %s has word list = %s%n", k, v));
    }


    public String getDictionaryFilePath() {
        return dictionaryFilePath;
    }

    public Map<String, Set<String>> getDictionary() {
        return dictionary;
    }

}
