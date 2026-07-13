package ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest;

import ar.com.paas.active.cluster.service.domain.exception.ClusterStateNotFoundException;
import ar.com.paas.active.cluster.service.domain.exception.ErrorCode;
import ar.com.paas.active.cluster.service.domain.exception.InvalidParametersException;
import ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto.ApiError;
import ar.com.paas.active.cluster.service.infrastructure.adapter.in.rest.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Manejador global de excepciones que devuelve siempre el envelope PaaS.
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InvalidParametersException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidParameters(InvalidParametersException ex) {
        log.warn("Parametros invalidos: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(ClusterStateNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ClusterStateNotFoundException ex) {
        log.warn("Estado no encontrado: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccess(DataAccessException ex) {
        log.error("Error de acceso a datos / Mongo no disponible", ex);
        return build(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.SERVICE_UNAVAILABLE.code(),
                "Servicio de datos no disponible");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception ex) {
        log.error("Error interno no controlado", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR.code(),
                "Error interno del servidor");
    }

    private ResponseEntity<ApiResponse<Object>> build(HttpStatus status, String code, String message) {
        ApiResponse<Object> body = ApiResponse.error("ERROR", "error",
                List.of(new ApiError(code, message)));
        return ResponseEntity.status(status).body(body);
    }
}
