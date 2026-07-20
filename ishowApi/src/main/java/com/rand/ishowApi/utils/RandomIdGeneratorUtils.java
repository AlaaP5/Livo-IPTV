package com.rand.ishowApi.utils;

import java.util.Random;
import java.util.UUID;

public class RandomIdGeneratorUtils {

    public static String generateRandomDigits(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);
    }

    // Generate UUID with hyphens
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // Generate UUID without hyphens (often used for IDs)
    public static String generateUUIDWithoutHyphens() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

