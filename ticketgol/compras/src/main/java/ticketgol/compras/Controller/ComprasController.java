package ticketgol.compras.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.compras.Dto.CompraRequestDTO;
import ticketgol.compras.Dto.CompraResponseDTO;
import ticketgol.compras.Service.ComprasService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compras")
@Tag(name = "Compras", description = "Operaciones de venta vinculadas a controles de sanción en red.")
public class ComprasController {

    private final ComprasService service;

    public ComprasController(ComprasService service) {
        this.service = service;
    }

    // ------------------------------------- READ ALL ------------------------------------------------
    @GetMapping
    @Operation(summary = "Obtener todas las compras")
    public ResponseEntity<List<CompraResponseDTO>> listarCompras() {
        List<CompraResponseDTO> compras = service.findAllCompras();
        if (compras.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(compras);
    }

    // ------------------------------------- READ BY ID ----------------------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Buscar compra por ID")
    public ResponseEntity<CompraResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findCompraById(id));
    }

    // ------------------------------------- CREATE (POST) --------------------------------------------
    @PostMapping
    @Operation(summary = "Registrar una nueva compra")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra exitosa"),
            @ApiResponse(responseCode = "400", description = "Usuario o Ticket no existen"),
            @ApiResponse(responseCode = "403", description = "Bloqueado: El usuario está sancionado")
    })
    public ResponseEntity<CompraResponseDTO> crearCompra(@Valid @RequestBody CompraRequestDTO request) {
        CompraResponseDTO nuevaCompra = service.saveCompra(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCompra);
    }

    // ------------------------------------- UPDATE (PUT) ---------------------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una compra existente")
    public ResponseEntity<CompraResponseDTO> actualizarCompra(@PathVariable Long id, @Valid @RequestBody CompraRequestDTO request) {
        // Mapeamos los datos del request a la estructura que tu Service necesite si agregas la actualización del CRUD
        // Por ahora, este método llama a la lógica de actualización usando tus DTOs.
        return ResponseEntity.ok(service.updateCompra(id, request));
    }

    // ------------------------------------- DELETE ---------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una compra")
    public ResponseEntity<String> eliminarCompra(@PathVariable Long id) {
        String mensaje = service.deleteCompra(id);
        return ResponseEntity.ok(mensaje);
    }
}