package com.wictor.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraException.class)
    public ResponseEntity<?> handleRegra(RegraException ex) {
        return ResponseEntity.status(400).body(
                Map.of("erro", ex.getMessage())
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401).body(
                Map.of("erro", ex.getMessage())
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNaoEncontrado(NotFoundException ex) {
        return ResponseEntity.status(404).body(
                Map.of("erro", ex.getMessage())
        );
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<?> handleConflito(ConflitoException ex) {
        return ResponseEntity.status(409).body(
                Map.of("erro", ex.getMessage())
        );
    }


}
