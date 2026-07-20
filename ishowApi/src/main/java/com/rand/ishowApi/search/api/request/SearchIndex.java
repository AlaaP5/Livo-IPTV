package com.rand.ishowApi.search.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum SearchIndex {
    ALL,
    MOVIES,
    SERIES,
    TV_PROGRAM,
    CLIPS,
    CHANNELS;

    @JsonCreator
    public static SearchIndex from(String value) {
        if (value == null || value.isBlank()) {
            return ALL;
        }

        String normalized = value.trim()
                .toUpperCase(Locale.ROOT)
                .replace("-", "_")
                .replace(" ", "_");

        return switch (normalized) {
            case "ALL" -> ALL;
            case "MOVIE", "MOVIES" -> MOVIES;
            case "SERIES" -> SERIES;
            case "TV", "TV_PROGRAM", "TV_PROGRAMS", "TVPROGRAM" -> TV_PROGRAM;
            case "CLIP", "CLIPS" -> CLIPS;
            case "CHANNEL", "CHANNELS" -> CHANNELS;
            default -> ALL;
        };
    }
}
