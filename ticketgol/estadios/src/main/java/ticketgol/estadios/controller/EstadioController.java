package ticketgol.estadios.controller;

import ticketgol.estadios.model.Estadio;
import ticketgol.estadios.service.EstadioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @GetMapping
    public ResponseEntity<List<Estadio>> lista() {
        List<Estadio> estadios = estadioService.findAll();

        if (estadios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadios);
    }

    @PostMapping
    public ResponseEntity<Estadio> guardar(@Valid @RequestBody Estadio estadio) {
        Estadio nuevo = estadioService.save(estadio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estadio> buscar(@PathVariable Long id) {
        try {
            Estadio estadio = estadioService.findById(id);
            return ResponseEntity.ok(estadio);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estadio> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Estadio estadio) {

        try {
            Estadio existente = estadioService.findById(id);

            existente.setId(id);
            existente.setNombre(estadio.getNombre());
            existente.setCiudad(estadio.getCiudad());
            existente.setCapacidad(estadio.getCapacidad());
            existente.setDireccion(estadio.getDireccion());

            estadioService.save(existente);

            return ResponseEntity.ok(existente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            estadioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}