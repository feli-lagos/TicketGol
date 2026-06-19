package ticketgol.eventos.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.eventos.dto.EventoDTO;
import ticketgol.eventos.service.EventoService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<EventoDTO> crearEvento(@Valid @RequestBody EventoDTO eventoDto) {
        log.info("Recibida petición POST para crear un nuevo evento");
        EventoDTO nuevoEvento = eventoService.guardarEvento(eventoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEvento);
    }

    @GetMapping
    public ResponseEntity<List<EventoDTO>> listarEventos() {
        log.info("Recibida petición GET para listar todos los eventos");
        List<EventoDTO> eventos = eventoService.obtenerTodosLosEventos();
        return ResponseEntity.ok(eventos);
    }
}