package ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto;

/**
 * Representa un error dentro del envelope PaaS.
 */
public record ApiError(String code, String message) {
}
