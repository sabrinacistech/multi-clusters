package ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto;

import java.util.List;

/**
 * Envelope generico de respuesta PaaS: { meta, data, errors }.
 */
public record ApiResponse<T>(Meta meta, T data, List<ApiError> errors) {

    public static <T> ApiResponse<T> ok(String method, String operation, T data) {
        return new ApiResponse<>(new Meta(method, operation), data, List.of());
    }

    public static <T> ApiResponse<T> error(String method, String operation, List<ApiError> errors) {
        return new ApiResponse<>(new Meta(method, operation), null, errors);
    }
}
