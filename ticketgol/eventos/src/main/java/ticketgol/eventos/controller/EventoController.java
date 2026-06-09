package ticketgol.eventos.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.eventos.dto.EventoDTO;
import ticketgol.eventos.service.EventoService;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<EventoDTO> crearEvento(@Valid @RequestBody EventoDTO eventoDto) {
        EventoDTO nuevoEvento = eventoService.guardarEvento(eventoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEvento);
    }
}