package com.sabrinacistech.multiclusters.service;

/**
 * Razones o causas de un evento (especialmente para errores o fallbacks).
 */
public enum ClusterReason {
    CONFIG_NOT_FOUND("configuration_missing"), RESOLUTION_ERROR("dns_resolution_failed"),
    CIRCUIT_OPEN("circuit_breaker_open"), STATE_MISMATCH("status_mismatch_detected"),
    NO_CHANGES("no_version_changes_detected"),
    INVALID_VALUE("invalid_polling_interval"),
    VERSION_UPDATE("newer_version_found"),
    STATUS_UNAVAILABLE("status_unavailable"),
    INVALID_POLLING_INTERVAL("invalid_polling_interval");

    private final String value;

    ClusterReason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
