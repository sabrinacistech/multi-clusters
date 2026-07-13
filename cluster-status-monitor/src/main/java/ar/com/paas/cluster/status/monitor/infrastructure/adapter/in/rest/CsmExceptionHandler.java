package ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest;

import ar.com.paas.cluster.status.monitor.domain.exception.BusinessRuleException;
import ar.com.paas.cluster.status.monitor.domain.exception.ConflictException;
import ar.com.paas.cluster.status.monitor.domain.exception.NotFoundException;
import ar.com.paas.cluster.status.monitor.domain.exception.ErrorCode;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.ApiError;
import ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones. Devuelve el sobre PaaS con la lista de errores poblada.
 * Diseñado para funcionar también bajo MockMvc standalone.
 */
@Slf4j
@RestControllerAdvice
public class CsmExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, ErrorCode.CSM_001,
                detail.isBlank() ? "Payload invalido" : detail);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ErrorCode.CSM_004, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
        return build(HttpStatus.CONFLICT, ErrorCode.CSM_009, ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessRule(BusinessRuleException ex) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.CSM_022, ex.getMessage());
    }

    @ExceptionHandler({DataAccessResourceFailureException.class, DataAccessException.class})
    public ResponseEntity<ApiResponse<Void>> handlePersistence(DataAccessException ex) {
        log.warn("CSM-007 base de datos no disponible: {}", ex.getMessage());
        return build(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.CSM_007,
                "Persistencia no disponible");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.error("CSM-500 error inesperado: {}", ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.CSM_500,
                "Error interno del servidor");
    }

    private String formatFieldError(FieldError fe) {
        return fe.getField() + ": " + fe.getDefaultMessage();
    }

    private ResponseEntity<ApiResponse<Void>> build(HttpStatus status, String code, String message) {
        ApiResponse<Void> body = new ApiResponse<>(
                new ar.com.paas.cluster.status.monitor.infrastructure.adapter.in.rest.dto.Meta(
                        "ERROR", "error"),
                null,
                List.of(new ApiError(code, message)));
        return ResponseEntity.status(status).body(body);
    }
}
