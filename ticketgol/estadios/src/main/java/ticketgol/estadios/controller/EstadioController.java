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
    public ResponseEntity<List<Estadio>> listar() {
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
    public ResponseEntity<Estadio> buscarPorId(@PathVariable Long id) {
        // No try-catch needed. If not found, GlobalExceptionHandler handles the 404 JSON response.
        Estadio estadio = estadioService.findById(id);
        return ResponseEntity.ok(estadio);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Estadio>> buscarPorNombre(@PathVariable String nombre) {
        List<Estadio> estadios = estadioService.findByNombre(nombre);
        if (estadios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadios);
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Estadio>> buscarPorCiudad(@PathVariable String ciudad) {
        List<Estadio> estadios = estadioService.findByCiudad(ciudad);
        if (estadios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estadio> actualizar(@PathVariable Long id, @Valid @RequestBody Estadio estadio) {
        // Cleaned up try-catch. Service handles the presence check automatically.
        Estadio existente = estadioService.findById(id);

        existente.setNombre(estadio.getNombre());
        existente.setCiudad(estadio.getCiudad());
        existente.setCapacidad(estadio.getCapacidad());
        existente.setDireccion(estadio.getDireccion());

        estadioService.save(existente);
        return ResponseEntity.ok(existente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}