package com.rand.ishowApi.utils;

public class PasswordUtils {
    public static String maskPassword(String password) {
        if (password == null || password.length() <= 4) {
            return "****";
        }
        return password.substring(0, 2) + "****" + password.substring(password.length() - 2);
    }
}
