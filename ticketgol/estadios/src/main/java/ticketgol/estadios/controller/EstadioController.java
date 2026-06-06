package ticketgol.estadios.controller;

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
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @GetMapping
    public ResponseEntity<List<Estadio>> listarTodos() {
        return ResponseEntity.ok(estadioService.findAll());
    }

    @PostMapping
    public ResponseEntity<Estadio> guardar(@Valid @RequestBody Estadio estadio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadioService.save(estadio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estadio> buscarPorId(@PathVariable Long id) {
        // Al usar tu service con orElseThrow, el Handler atrapa el 404 limpiamente
        Estadio estadio = estadioService.findById(id);
        return ResponseEntity.ok(estadio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estadio> actualizar(@PathVariable Long id, @Valid @RequestBody Estadio estadio) {
        Estadio actualizado = estadioService.update(id, estadio);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}