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
    private RegistroAccesoService service;

    @GetMapping
    public List<RegistroAcceso> listarAccesos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroAcceso> buscarPorId(@PathVariable Long id) {
        RegistroAcceso acceso = service.obtenerPorId(id);
        return ResponseEntity.ok(acceso);
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
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}