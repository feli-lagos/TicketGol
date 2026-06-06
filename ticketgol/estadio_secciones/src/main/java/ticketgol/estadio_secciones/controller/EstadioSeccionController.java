package ticketgol.estadio_secciones.controller;

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
public class EstadioSeccionController {

    @Autowired
    private EstadioSeccionService seccionService;

    @GetMapping
    public ResponseEntity<List<EstadioSeccion>> listar() {
        List<EstadioSeccion> secciones = seccionService.findAll();
        if (secciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(secciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioSeccion> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(seccionService.findById(id));
    }

    @GetMapping("/estadio/{estadioId}")
    public ResponseEntity<List<EstadioSeccion>> buscarPorEstadio(@PathVariable Long estadioId) {
        List<EstadioSeccion> secciones = seccionService.findByEstadioId(estadioId);
        if (secciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(secciones);
    }

    @PostMapping
    public ResponseEntity<EstadioSeccion> guardar(@Valid @RequestBody EstadioSeccion seccion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seccionService.save(seccion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadioSeccion> actualizar(@PathVariable Long id, @Valid @RequestBody EstadioSeccion seccion) {
        return ResponseEntity.ok(seccionService.update(id, seccion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        seccionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}