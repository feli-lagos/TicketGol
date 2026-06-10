package ticketgol.compras.Controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Service.ComprasService;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    private final ComprasService service;

    public ComprasController(ComprasService service){

        this.service = service;
    }

    @GetMapping
    public List<Compras> listarCompras(){

        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compras> buscarPorId(@PathVariable Long id){
        Compras compra = service.obtenerPorId(id);
        return ResponseEntity.ok(compra);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Compras> listPorUsuario(@PathVariable String usuarioId){
        return service.obtenerPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<Compras> crearCompra(@Valid @RequestBody Compras compras){
        Compras nuevaCompra = service.guardar(compras);
        return new ResponseEntity<>(nuevaCompra, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.noContent().build(); // Retorna un código 204 exitoso sin cuerpo
    }
}