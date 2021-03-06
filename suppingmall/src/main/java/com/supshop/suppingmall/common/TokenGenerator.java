package com.supshop.suppingmall.common;

import java.util.Random;


public class TokenGenerator {

    private static final char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    private static final int tokenLength = 12;


    private static final char[] passwordTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z' , '~', '․', '!', '@',  '#',  '$', '%', '^', '&', '*', '(', ')', '_', '-',
            '+', '=',  '[', ']', '[', ']', '|', '\\', ':', '‘', '“', '<', '>', ',', '.', '?', '/'
    };

    private static final int passwordLength = 16;


    public static String issueToken() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        int tableLength = characterTable.length;

        for(int i = 0; i < tokenLength; i++ ) {
            sb.append(characterTable[random.nextInt(tableLength)]);
        }

        return sb.toString();
    }

    public static String issuePassword() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        int tableLength = characterTable.length;

        for(int i = 0; i < tokenLength; i++ ) {
            sb.append(characterTable[random.nextInt(tableLength)]);
        }

        return sb.toString();
    }

}
