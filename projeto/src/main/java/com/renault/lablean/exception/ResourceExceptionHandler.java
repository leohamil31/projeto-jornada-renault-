package com.renault.lablean.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; // Retorna 404
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Recurso não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<java.util.Map<String, Object>> validationError(MethodArgumentNotValidException e, HttpServletRequest request) {
    java.util.List<String> mensagens = e.getBindingResult().getFieldErrors().stream()
            .map(f -> f.getField() + ": " + f.getDefaultMessage())
            .toList();

    java.util.Map<String, Object> body = new java.util.LinkedHashMap<>();
    body.put("timestamp", Instant.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Erro de Validação");
    body.put("message", mensagens.isEmpty() ? "Dados inválidos" : mensagens.get(0));
    body.put("mensagens", mensagens);
    body.put("path", request.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
}
}