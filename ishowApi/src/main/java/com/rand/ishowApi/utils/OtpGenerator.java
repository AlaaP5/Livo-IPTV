package com.rand.ishowApi.utils;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    public static String generateOtp() {

       return "000000"; // for test
        
        /*char[] digits = new char[OTP_LENGTH];
        // First digit 1-9 to avoid leading zero
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < OTP_LENGTH; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);*/
    }
}