package com.supshop.suppingmall.api.common;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String traceId = MDC.get("traceId");
        List<Map<String,Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.<String,Object>of(
                        "field", fe.getField(),
                        "message", fe.getDefaultMessage(),
                        "rejected", fe.getRejectedValue()))
                .toList();
        return ResponseEntity.badRequest().body(
                ApiResponse.fail(new ApiError("VALIDATION_ERROR", errors.toString()), traceId)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraint(ConstraintViolationException ex) {
        String traceId = MDC.get("traceId");
        return ResponseEntity.badRequest().body(
                ApiResponse.fail(new ApiError("CONSTRAINT_VIOLATION", ex.getMessage()), traceId)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        String traceId = MDC.get("traceId");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.fail(new ApiError("INTERNAL_SERVER_ERROR", ex.getMessage()), traceId)
        );
    }
}