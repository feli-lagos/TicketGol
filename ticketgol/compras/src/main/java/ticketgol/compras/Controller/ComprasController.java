package ticketgol.compras.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.compras.Model.Compras;
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

    @GetMapping
    @Operation(summary = "Obtener todas las compras")
    public ResponseEntity<List<Compras>> listarCompras() {
        List<Compras> compras = service.obtenerTodos();
        if (compras.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar compra por ID")
    public ResponseEntity<Compras> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar compras por ID de Usuario")
    public ResponseEntity<List<Compras>> listPorUsuario(@PathVariable String usuarioId) {
        List<Compras> comprasUsuario = service.obtenerPorUsuario(usuarioId);
        if (comprasUsuario.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(comprasUsuario);
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva compra")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra exitosa"),
            @ApiResponse(responseCode = "400", description = "Usuario o Ticket no existen"),
            @ApiResponse(responseCode = "403", description = "Bloqueado: El usuario está sancionado")
    })
    public ResponseEntity<Compras> crearCompra(@Valid @RequestBody Compras compras) {
        Compras nuevaCompra = service.guardar(compras);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCompra);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una compra")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}