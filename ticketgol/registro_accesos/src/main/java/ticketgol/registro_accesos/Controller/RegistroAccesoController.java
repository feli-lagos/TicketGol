package ticketgol.registro_accesos.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.registro_accesos.Modelo.RegistroAcceso;
import ticketgol.registro_accesos.Service.RegistroAccesoService;

import java.util.List;

@RestController
@RequestMapping("/api/registro-accesos")
public class RegistroAccesoController {
    @Autowired
    private RegistroAccesoService service; // <-- Cambiado al tipo de clase correcto

    @GetMapping
    public List<RegistroAcceso> listarAccesos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroAcceso> buscarPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guardia/{guardiaid}")
    public List<RegistroAcceso> listarPorGuardia(@PathVariable Long guardiaid) {
        return service.obtenerPorGuardia(guardiaid);
    }

    @PostMapping
    public ResponseEntity<RegistroAcceso> crearAcceso(@Valid @RequestBody RegistroAcceso registroAcceso) {
        RegistroAcceso nuevoAcceso = service.guardarAcceso(registroAcceso);
        return new ResponseEntity<>(nuevoAcceso, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAcceso(@PathVariable Long id) {
        if (service.obtenerPorId(id).isPresent()) {
            service.eliminar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
