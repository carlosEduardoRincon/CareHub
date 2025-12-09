package com.care.hub.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKey(DuplicateKeyException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT,
                "Registro já existe (violação de chave única).",
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT,
                "Violação de integridade de dados.",
                request.getRequestURI(),
                null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                            HttpServletRequest request) {
        List<Map<String, Object>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toFieldErrorPayload)
                .toList();

        return build(HttpStatus.BAD_REQUEST,
                "Erro de validação dos campos.",
                request.getRequestURI(),
                fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex,
                                                                         HttpServletRequest request) {
        List<Map<String, Object>> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(v -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("field", v.getPropertyPath() != null ? v.getPropertyPath().toString() : null);
                    m.put("message", v.getMessage());
                    String code = null;
                    if (v.getConstraintDescriptor() != null && v.getConstraintDescriptor().getAnnotation() != null) {
                        code = v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
                    }
                    m.put("code", code);
                    return m;
                })
                .toList();

        return build(HttpStatus.BAD_REQUEST,
                "Erro de validação.",
                request.getRequestURI(),
                fieldErrors);
    }

    private Map<String, Object> toFieldErrorPayload(FieldError fe) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("field", fe.getField());
        m.put("message", fe.getDefaultMessage());
        m.put("code", fe.getCode());
        Object rejected = fe.getRejectedValue();

        // Evitar expor dados sensíveis (password/senha). Caso contrário, incluir o valor rejeitado.
        if (rejected != null) {
            String f = fe.getField();
            if ("password".equalsIgnoreCase(f) || "senha".equalsIgnoreCase(f)) {
                m.put("rejectedValue", "***");
            } else {
                m.put("rejectedValue", rejected);
            }
        }
        return m;
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status,
                                                      String message,
                                                      String path,
                                                      List<Map<String, Object>> fieldErrors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            body.put("fields", fieldErrors);
        }
        return ResponseEntity.status(status).body(body);
    }
}