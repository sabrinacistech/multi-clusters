package com.sabrinacistech.multiclusters.common;

import org.springframework.boot.context.properties.*;
import org.springframework.stereotype.*;
import org.springframework.validation.annotation.*;

import jakarta.annotation.PostConstruct;


@Validated
@Component
@ConfigurationProperties(prefix = "cluster-status")
public class ClusterConfigProperties implements ClusterConfig {
    private String commandURL;
    private String dataCenter;
    private String intHostAlias;
    private String hostOverride;
    private Boolean active;
    private Integer cacheTtlSeconds;
    private Integer scheduleDelaySeconds;

    public String getCommandURL() {
        return commandURL;
    }

    public void setCommandURL(String commandURL) {
        this.commandURL = commandURL;
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(String dataCenter) {
        this.dataCenter = dataCenter;
    }

    /**
     * Indica el nombre del host que identifica el origen o la ubicación del clúster.
     * El valor se setea al pod en el momento de la creación del mismo, y se espera que sea el mismo para todos los pods del clúster.
     *
     * @return String
     */
    public String getIntHostAlias() {
        return intHostAlias;
    }

    public void setIntHostAlias(String intHostAlias) {
        this.intHostAlias = intHostAlias;
    }

    public String getHostOverride() {
        return hostOverride;
    }

    public void setHostOverride(String hostOverride) {
        this.hostOverride = hostOverride;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getCacheTtlSeconds() {
        return cacheTtlSeconds;
    }

    public void setCacheTtlSeconds(Integer cacheTtlSeconds) {
        this.cacheTtlSeconds = cacheTtlSeconds;
    }

    public Integer getScheduleDelaySeconds() {
        return scheduleDelaySeconds;
    }

    public void setScheduleDelaySeconds(Integer scheduleDelaySeconds) {
        this.scheduleDelaySeconds = scheduleDelaySeconds;
    }

    /*
     * Asegura que si se configuró hostOverride en el ambiente de Integracion, igual al dataCenter, entonces se setee el dataCenter con el valor del intHostAlias (en mayúscula).
     * El uso de @PostConstruct garantiza que esta lógica se ejecute después de que todas las properties hayan sido seteadas por Spring.
     * Es importante que esta lógica se ejecute solo una vez al inicio, por eso se
     */
    @PostConstruct
    public void init() {
        if (intHostAlias != null && hostOverride != null && hostOverride.equalsIgnoreCase(dataCenter)) {
            this.dataCenter = this.intHostAlias.toUpperCase();
        }
    }
}

