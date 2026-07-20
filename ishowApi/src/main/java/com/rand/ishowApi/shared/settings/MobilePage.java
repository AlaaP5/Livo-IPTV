package com.rand.ishowApi.shared.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MobilePage {
    HOME("Home"),
    SPORT("Sport"),
    KIDS("kids"),
    CHANNELS("channels"),
    MOVIES("movies"),
    SERIES("series"),
    TV_PROGRAMS("TV programs"),
    CLIPS("clips");

    private final String value;

    MobilePage(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MobilePage fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = normalize(value);

        for (MobilePage page : values()) {
            if (normalize(page.value).equals(normalized) || normalize(page.name()).equals(normalized)) {
                return page;
            }
        }

        throw new IllegalArgumentException("Unsupported mobile page: " + value);
    }

    private static String normalize(String value) {
        return value.trim().replaceAll("[\\s-]+", "_").toUpperCase();
    }
}