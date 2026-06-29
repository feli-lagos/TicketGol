package ticketgol.eventos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Eventos", description = "Operaciones relacionadas a la gestión de eventos deportivos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los eventos", description = "Obtiene una lista de todos los eventos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    public ResponseEntity<List<EventoDTO>> listarEventos() {
        log.info("Recibida petición GET para listar todos los eventos");
        return ResponseEntity.ok(eventoService.obtenerTodosLosEventos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID", description = "Obtiene un evento específico a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<EventoDTO> buscarPorId(@PathVariable Long id) {
        log.info("Recibida petición GET para buscar el evento con ID: {}", id);
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo evento", description = "Crea un nuevo evento validando la existencia de los clubes locales y visitantes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación de datos"),
            @ApiResponse(responseCode = "404", description = "Uno o ambos clubes ingresados no existen")
    })
    public ResponseEntity<EventoDTO> registrarEvento(@Valid @RequestBody EventoDTO eventoDto) {
        log.info("Recibida petición POST para registrar un nuevo evento");
        EventoDTO nuevoEvento = eventoService.guardarEvento(eventoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEvento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un evento existente", description = "Modifica los datos de un evento a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación de datos"),
            @ApiResponse(responseCode = "404", description = "Evento o Club no encontrado")
    })
    public ResponseEntity<EventoDTO> actualizarEvento(@PathVariable Long id, @Valid @RequestBody EventoDTO eventoDto) {
        log.info("Recibida petición PUT para actualizar el evento con ID: {}", id);
        EventoDTO eventoActualizado = eventoService.actualizarEvento(id, eventoDto);
        return ResponseEntity.ok(eventoActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un evento", description = "Elimina un evento del registro a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    public ResponseEntity<String> eliminarEvento(@PathVariable Long id) {
        log.info("Recibida petición DELETE para eliminar el evento con ID: {}", id);
        eventoService.eliminarEvento(id);
        return ResponseEntity.ok("Evento eliminado correctamente");
    }
}