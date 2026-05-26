package com.sabrinacistech.multiclusters.util;

public final class LogSanitizer {

    private LogSanitizer() {
    }

    public static String sanitizeForLog(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }

        return value
            .replaceAll("[\\r\\n\\t]", "_")
            .replaceAll("[^\\p{Alnum} ._:@/-]", "_");
    }
}
