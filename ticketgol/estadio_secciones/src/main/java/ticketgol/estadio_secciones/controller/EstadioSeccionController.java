package ticketgol.estadio_secciones.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.estadio_secciones.model.EstadioSeccion;
import ticketgol.estadio_secciones.service.EstadioSeccionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secciones")
@Tag(name = "Secciones de Estadios", description = "Todas las operaciones de gestión de las secciones o sectores internos de los estadios.")
public class EstadioSeccionController {

    @Autowired
    private EstadioSeccionService seccionService;

    @GetMapping
    @Operation(summary = "Obtener todas las secciones", description = "Obtiene una lista global de todas las secciones de estadios registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de secciones recuperada con éxito"),
            @ApiResponse(responseCode = "204", description = "No hay secciones registradas en el sistema")
    })
    public ResponseEntity<List<EstadioSeccion>> listar() {
        List<EstadioSeccion> secciones = seccionService.findAll();
        if (secciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(secciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sección por ID", description = "Busca los detalles específicos de una sección utilizando su identificador numérico interno (ID).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sección encontrada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadioSeccion.class))),
            @ApiResponse(responseCode = "404", description = "ID de sección no encontrado")
    })
    public ResponseEntity<EstadioSeccion> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(seccionService.findById(id));
    }

    @GetMapping("/estadio/{estadioId}")
    @Operation(summary = "Buscar secciones por ID de Estadio", description = "Obtiene una lista de todas las secciones o sectores pertenecientes a un estadio específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Secciones del estadio recuperadas con éxito"),
            @ApiResponse(responseCode = "204", description = "No se encontraron secciones asociadas al ID del estadio provisto")
    })
    public ResponseEntity<List<EstadioSeccion>> buscarPorEstadio(@PathVariable Long estadioId) {
        List<EstadioSeccion> secciones = seccionService.findByEstadioId(estadioId);
        if (secciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(secciones);
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva sección", description = "Crea una nueva sección o sector asignado a un estadio, especificando su capacidad o precio base.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sección creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadioSeccion.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o mal estructurados")
    })
    public ResponseEntity<EstadioSeccion> guardar(@Valid @RequestBody EstadioSeccion seccion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seccionService.save(seccion));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una sección", description = "Modifica los detalles de una sección de estadio existente buscando por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sección actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadioSeccion.class))),
            @ApiResponse(responseCode = "404", description = "Sección no encontrada para actualizar"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<EstadioSeccion> actualizar(@PathVariable Long id, @Valid @RequestBody EstadioSeccion seccion) {
        return ResponseEntity.ok(seccionService.update(id, seccion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una sección", description = "Remueve definitivamente el registro de una sección de estadio del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sección eliminada exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Sección no encontrada para eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        seccionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}