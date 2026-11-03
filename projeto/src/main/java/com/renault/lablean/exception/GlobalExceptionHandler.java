package com.renault.lablean.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Captura qualquer RuntimeException lançada pelos seus Services
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", LocalDateTime.now());
        
        // Vamos assumir que RuntimeExceptions genéricas de busca são 404 (Not Found)
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Não Encontrado");
        
        // Aqui o Spring injeta a sua mensagem original ("Auditoria 5S não encontrada...")
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}