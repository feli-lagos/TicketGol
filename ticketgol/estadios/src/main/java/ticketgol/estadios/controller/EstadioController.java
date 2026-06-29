package ticketgol.estadios.controller;

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
import ticketgol.estadios.model.Estadio;
import ticketgol.estadios.service.EstadioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estadios")
@Tag(name = "Estadios", description = "Todas las operaciones de gestión de recintos deportivos (estadios).")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @GetMapping
    @Operation(summary = "Obtener todos los estadios", description = "Obtiene una lista completa de todos los estadios registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estadios recuperada con éxito"),
            @ApiResponse(responseCode = "204", description = "No hay estadios registrados en el sistema")
    })
    public ResponseEntity<List<Estadio>> listarTodos() {
        List<Estadio> estadios = estadioService.findAll();
        if (estadios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadios);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo estadio", description = "Crea un nuevo registro de estadio en el sistema con su respectiva capacidad y locación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estadio creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estadio.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o mal estructurados")
    })
    public ResponseEntity<Estadio> guardar(@Valid @RequestBody Estadio estadio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioService.save(estadio));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estadio por ID", description = "Busca los detalles de un estadio utilizando su identificador numérico interno (ID).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadio encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estadio.class))),
            @ApiResponse(responseCode = "404", description = "ID de estadio no encontrado")
    })
    public ResponseEntity<Estadio> buscarPorId(@PathVariable Long id) {
        Estadio estadio = estadioService.findById(id);
        return ResponseEntity.ok(estadio);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un estadio", description = "Modifica los datos de infraestructura o información de un estadio existente buscando por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadio actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Estadio.class))),
            @ApiResponse(responseCode = "404", description = "Estadio no encontrado para actualizar"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<Estadio> actualizar(@PathVariable Long id, @Valid @RequestBody Estadio estadio) {
        Estadio actualizado = estadioService.update(id, estadio);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estadio", description = "Remueve definitivamente el registro de un estadio del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estadio eliminado exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Estadio no encontrado para eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}