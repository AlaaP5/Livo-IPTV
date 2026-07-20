package com.rand.ishowApi.shared.settings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContentType {
    CHANNELS("channels"),
    MOVIES("movies"),
    SERIES("series"),
    TV_PROGRAMS("tv_programs"),
    CLIPS("clips");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ContentType fromValue(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = normalize(value);

        for (ContentType contentType : values()) {
            if (normalize(contentType.value).equals(normalized) || normalize(contentType.name()).equals(normalized)) {
                return contentType;
            }
        }

        throw new IllegalArgumentException("Unsupported content type: " + value);
    }

    private static String normalize(String value) {
        return value.trim().replaceAll("[\\s-]+", "_").toUpperCase();
    }
}