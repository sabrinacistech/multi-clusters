package com.sabrinacistech.multiclusters.model;

public class Cluster {
    private final String alias;
    private final String url;
    private final int pollingIntervalSeconds;
    private final boolean active;

    public Cluster(String alias, String url, boolean active, int pollingIntervalSeconds) {
        if (alias == null || alias.isEmpty()) throw new IllegalArgumentException("alias requerido");
        if (url == null || url.isEmpty()) throw new IllegalArgumentException("url requerida");
        if (pollingIntervalSeconds <= 0) throw new IllegalArgumentException("pollingIntervalSeconds > 0");
        this.alias = alias;
        this.url = url;
        this.pollingIntervalSeconds = pollingIntervalSeconds;
        this.active = active;
    }


    public String getAlias() {
        return alias;
    }

    public String getUrl() {
        return url;
    }

    public int getPollingIntervalSeconds() {
        return pollingIntervalSeconds;
    }

    public boolean isActive() {
        return active;
    }

    public Cluster withActive(boolean newActive) {
        return new Cluster(alias, url, newActive, pollingIntervalSeconds);
    }

}