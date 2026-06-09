package ticketgol.clubes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarClubNoEncontrado(ClubNotFoundException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.NOT_FOUND.value());
        respuesta.put("error", "Club no encontrado");
        respuesta.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(WebExchangeBindException ex) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("status", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Error en los datos enviados");


        Map<String, String> erroresDetallados = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erroresDetallados.put(error.getField(), error.getDefaultMessage());
        }
        respuesta.put("detalles", erroresDetallados);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}