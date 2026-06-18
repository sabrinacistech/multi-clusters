package com.sabrinacistech.multiclusters.enums;

import lombok.*;

@Getter
public enum LogKey {
    // Llaves de campos (CWE-117 compliant)
    CLUSTER("cluster={}"), EVENT("event={}"), ACTION("action={}"),
    STATUS("status={}"), REASON("reason={}"), OLD_DELAY("old_delay={}s"),
    NEW_DELAY("new_delay={}s"), VERSION("current_version={}"), APP("app={}"),
    PROJECT("project={}"), URI("uri={}"),
    BASE_TEMPLATE("cluster={} event={} action={} reason={}"),
    BASE_VALUE_TEMPLATE("cluster={} event={} action={} reason={} value={}"),

    // Technical Templates for Grafana/Loki
    HOSTNAME_VERIFICATION_SUCCESS("cluster={} is_active={} event={} resolved_host={} status=success"),
    HOSTNAME_VERIFICATION_FAILURE("cluster={} is_active={} event={} action={} reason={} status=failed"),

    HEARTBEAT("cluster={} is_active={} event=heartbeat status=alive "),
    UPDATE_CALCULATION("cluster={} is_active={} event={} action={} old_delay={}s new_delay={}s version={}"),
    CONFIG_NOT_FOUND("cluster={} is_active={} event={} action={} reason={}"),
    QUERY_CONFIG_NOT_FOUND("cluster={} event={} action={} reason={}"),
    INVALID_VALUE("cluster={} is_active={} event={} action={} reason={} value={}"),
    NO_CHANGES_DEBUG("cluster={} event={} action={} status={} version={}"),
    REQUEST_RECEIVED("cluster={} reported_cluster={} event={} app_id={} project={} "),
    REQUEST_REJECTED("cluster={} event={} action={} reason={}"),
    REQUEST_SUCCESS("cluster={} reported_cluster={} event={} is_active={} polling_interval_seconds={} resource_path={}");

    private final String value;

    LogKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}