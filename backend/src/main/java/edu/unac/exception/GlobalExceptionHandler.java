package edu.unac.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSearchException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidSearch(InvalidSearchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "success", false,
                        "error", ex.getMessage(),
                        "code", "INVALID_SEARCH"
                ));
    }

    @ExceptionHandler(NotEnoughResultsException.class)
    public ResponseEntity<Map<String, Object>> handleNotEnoughResults(NotEnoughResultsException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "success", false,
                        "error", ex.getMessage(),
                        "code", "NOT_ENOUGH_RESULTS"
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "success", false,
                        "error", ex.getMessage(),
                        "code", "INTERNAL_ERROR"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "success", false,
                        "error", ex.getMessage(),
                        "code", "UNEXPECTED_ERROR"
                ));
    }
}