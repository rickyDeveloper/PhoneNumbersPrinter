package com.naiyarv.enums;

/**
 * Enum to represent the encoding enum
 * Created by vikasnaiyar
 */
public enum NumberEncodingEnum {

    TWO('2', "ABC"),
    THREE('3', "DEF"),
    FOUR('4', "GHI"),
    FIVE('5', "JKL"),
    SIX('6', "MNO"),
    SEVEN('7', "PQRS"),
    EIGHT('8', "TUV"),
    NINE('9', "WXYZ");

    public char getDigit() {
        return digit;
    }

    public String getCharacters() {
        return characters;
    }

    private char digit;

    private String characters;

    NumberEncodingEnum(char digit, String characters) {
        this.digit = digit;
        this.characters = characters;
    }

}
