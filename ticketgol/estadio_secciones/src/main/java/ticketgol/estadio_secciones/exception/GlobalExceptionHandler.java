package ticketgol.estadio_secciones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstadioNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEstadioNotFoundException(EstadioNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(EstadioSeccionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEstadioSeccionNotFoundException(EstadioSeccionNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> validations = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validations.put(error.getField(), error.getDefaultMessage());
        }
        return buildValidationErrorResponse(validations);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(WebExchangeBindException ex) {
        Map<String, String> validations = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validations.put(error.getField(), error.getDefaultMessage());
        }
        return buildValidationErrorResponse(validations);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String errorLabel, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", errorLabel);
        errorResponse.put("message", message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private ResponseEntity<Map<String, Object>> buildValidationErrorResponse(Map<String, String> validations) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", "Validation failed for fields");
        errorResponse.put("validations", validations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}