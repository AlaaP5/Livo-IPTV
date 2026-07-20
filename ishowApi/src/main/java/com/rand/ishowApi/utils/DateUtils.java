package com.rand.ishowApi.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DateUtils {



    public static String normalize(String date) {
        if (date == null) return null;

        // add UTC if missing
        if (!date.endsWith("Z")) {
            date = date + "Z";
        }

        return Instant.parse(date)
                .truncatedTo(java.time.temporal.ChronoUnit.MILLIS)
                .toString();
    }


    public static String format(Instant instant) {
        if (instant == null) return null;

        return instant.truncatedTo(ChronoUnit.MILLIS).toString();
    }
}