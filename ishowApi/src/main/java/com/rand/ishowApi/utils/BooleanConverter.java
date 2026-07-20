package com.rand.ishowApi.utils;

public class BooleanConverter {

    public static Boolean getActiveBoolean(String value) {
        if (value == null || value.isBlank()) return null;

        if (value.equals("1") || value.equalsIgnoreCase("true"))
            return true;

        if (value.equals("0") || value.equalsIgnoreCase("false"))
            return false;

        return null;
    }
}

