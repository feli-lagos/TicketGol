package ticketgol.merch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.merch.dto.MerchandiseDTO;
import ticketgol.merch.service.MerchandiseService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/merchandise")
@Tag(name = "Merchandise", description = "Operaciones relacionadas a los artículos de la tienda")
public class MerchandiseController {

    private final MerchandiseService service;

    public MerchandiseController(MerchandiseService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los artículos", description = "Obtiene una lista de todo el merchandise disponible")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa")
    })
    public ResponseEntity<List<MerchandiseDTO>> listarArticulos() {
        log.info("Recibida petición GET para listar todos los artículos de merchandise");
        return ResponseEntity.ok(service.obtenerTodosLosArticulos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener artículo por ID", description = "Obtiene un artículo específico a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    public ResponseEntity<MerchandiseDTO> buscarPorId(@PathVariable Long id) {
        log.info("Recibida petición GET para buscar el artículo con ID: {}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo artículo", description = "Crea un nuevo artículo de merchandise validando el club asociado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artículo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación de datos"),
            @ApiResponse(responseCode = "404", description = "El club ingresado no existe")
    })
    public ResponseEntity<MerchandiseDTO> registrarArticulo(@Valid @RequestBody MerchandiseDTO dto) {
        log.info("Recibida petición POST para registrar un nuevo artículo");
        MerchandiseDTO nuevoArticulo = service.guardarArticulo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoArticulo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un artículo existente", description = "Modifica los datos de un artículo a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación de datos"),
            @ApiResponse(responseCode = "404", description = "Artículo o Club no encontrado")
    })
    public ResponseEntity<MerchandiseDTO> actualizarArticulo(@PathVariable Long id, @Valid @RequestBody MerchandiseDTO dto) {
        log.info("Recibida petición PUT para actualizar el artículo con ID: {}", id);
        MerchandiseDTO articuloActualizado = service.actualizarArticulo(id, dto);
        return ResponseEntity.ok(articuloActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un artículo", description = "Elimina un artículo del registro a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    public ResponseEntity<String> eliminarArticulo(@PathVariable Long id) {
        log.info("Recibida petición DELETE para eliminar el artículo con ID: {}", id);
        service.eliminarArticulo(id);
        return ResponseEntity.ok("Artículo eliminado correctamente");
    }
}