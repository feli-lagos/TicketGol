package ticketgol.guardias.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.guardias.model.Guardias;
import ticketgol.guardias.service.GuardiasService;
import java.util.List;

@RestController
@RequestMapping("/api/guardias")
public class GuardiasController {
    private final GuardiasService service;

    public GuardiasController(GuardiasService service) {
        this.service = service;
    }
    @GetMapping
    public List<Guardias> listarGuardias() {
        return service.obtenerTodos();
    }

    public ResponseEntity<Guardias> buscarPorId(@PathVariable Long id) {
        Guardias guardia = service.obtenerPorId(id);
        return ResponseEntity.ok(guardia);
    }
    @GetMapping("/estadio/{estadioId}")
    public List<Guardias> listarPorEstadio(@PathVariable Long estadioId) {
        return service.obtenerPorEstadio(estadioId);
    }
    @PostMapping
    public ResponseEntity<Guardias> crearGuardia(@Valid @RequestBody Guardias guardia) {
        Guardias nuevoGuardia = service.guardar(guardia);
        return new ResponseEntity<>(nuevoGuardia, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGuardia(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content si se borra con éxito
    }
}