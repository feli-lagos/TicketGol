package ticketgol.merch_ordenes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.merch_ordenes.model.MerchOrden;
import ticketgol.merch_ordenes.service.MerchOrdenService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/merch-orden")
@Tag(name = "Órdenes de Merchandising", description = "Operaciones relacionadas a las órdenes de compra de merch")
public class MerchOrdenController {

    private final MerchOrdenService merchOrdenService;

    public MerchOrdenController(MerchOrdenService merchOrdenService) {
        this.merchOrdenService = merchOrdenService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las órdenes", description = "Obtiene una lista de todas las órdenes de merchandising registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No hay órdenes registradas")
    })
    public ResponseEntity<List<MerchOrden>> listOrdenes() {
        log.info("Recibida petición GET para listar todas las órdenes de merch");
        List<MerchOrden> listOrdenes = merchOrdenService.findAllOrdenes();
        if (listOrdenes.isEmpty()) {
            log.warn("No se encontraron órdenes, retornando 204 No Content");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listOrdenes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Obtiene una orden de merchandising específica a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<MerchOrden> getOrden(@PathVariable Long id) {
        log.info("Recibida petición GET para buscar la orden con ID: {}", id);
        MerchOrden orden = merchOrdenService.getOrdenById(id);
        return ResponseEntity.ok(orden);
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva orden", description = "Crea una nueva orden de merchandising.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de orden inválidos")
    })
    public ResponseEntity<MerchOrden> createOrden(@Valid @RequestBody MerchOrden orden) {
        log.info("Recibida petición POST para registrar una nueva orden");
        MerchOrden ordenNueva = merchOrdenService.crearOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenNueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una orden existente", description = "Modifica los datos de una orden de merchandising a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<MerchOrden> updateOrden(@PathVariable Long id, @Valid @RequestBody MerchOrden ordenDetails) {
        log.info("Recibida petición PUT para actualizar la orden con ID: {}", id);
        MerchOrden ordenActualizada = merchOrdenService.updateOrden(id, ordenDetails);
        return ResponseEntity.ok(ordenActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una orden", description = "Elimina del registro una orden de merchandising a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity<String> deleteOrden(@PathVariable Long id) {
        log.info("Recibida petición DELETE para eliminar la orden con ID: {}", id);
        merchOrdenService.deleteOrden(id);
        return ResponseEntity.ok("Orden eliminada correctamente");
    }
}