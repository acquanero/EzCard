package com.acquanero.ezcard.myutils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyHashGenerator {

    public static String hashString(String word) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] digest = md.digest(word.getBytes());
        StringBuilder hashedWord = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            hashedWord.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }

        return hashedWord.toString();

    }
}
