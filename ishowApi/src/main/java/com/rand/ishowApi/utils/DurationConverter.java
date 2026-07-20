package com.rand.ishowApi.utils;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;

public class DurationConverter {

    // Seconds → HH:mm:ss
    public static String toTimeFormat(int totalSeconds) {
        if (totalSeconds < 0) {
            throw new CustomException(ApiResponseCode.DURATION_INVALID, null);
        }

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    // HH:mm:ss or mm:ss → Seconds
    public static int toSeconds(String time) {
        if (time == null || time.isEmpty()) {
            throw new CustomException(ApiResponseCode.DURATION_INVALID, null);
        }

        String[] parts = time.split(":");
        int seconds;

        try {
            if (parts.length == 3) {
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int secs = Integer.parseInt(parts[2]);

                seconds = (hours * 3600) + (minutes * 60) + secs;

            } else if (parts.length == 2) {
                int minutes = Integer.parseInt(parts[0]);
                int secs = Integer.parseInt(parts[1]);

                seconds = (minutes * 60) + secs;

            } else {
                throw new CustomException(ApiResponseCode.DURATION_INVALID, null);
            }
        } catch (NumberFormatException e) {
            throw new CustomException(ApiResponseCode.DURATION_INVALID, null);
        }

        return seconds;
    }

    // Seconds → Human readable
    public static String toHumanReadable(int totalSeconds) {
        if (totalSeconds < 0) {
            throw new CustomException(ApiResponseCode.DURATION_INVALID, null);
        }

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append(" hour");
            if (hours > 1) result.append("s");
        }

        if (minutes > 0) {
            if (hours > 0) result.append(" ");
            result.append(minutes).append(" min");
        }

        if (hours == 0 && minutes == 0) {
            result.append("0 min");
        }

        return result.toString();
    }

    // Total minutes (number)
    public static int toMinutes(int totalSeconds) {
        if (totalSeconds < 0) {
            throw new CustomException(ApiResponseCode.DURATION_INVALID, null);
        }

        return totalSeconds / 60;
    }

    // Raw minutes string
    public static String toRawMinutes(int totalSeconds) {
        if (totalSeconds < 0) {
            throw new CustomException(ApiResponseCode.DURATION_INVALID, null);
        }

        return (totalSeconds / 60) + " min";
    }

    // Helpers
    public static int getHours(int totalSeconds) {
        return totalSeconds / 3600;
    }

    public static int getRemainingMinutes(int totalSeconds) {
        return (totalSeconds % 3600) / 60;
    }
}