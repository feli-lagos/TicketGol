package ticketgol.usuarios_sancionados.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import ticketgol.usuarios_sancionados.service.UsuarioSancionadoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios-sancionados")
public class UsuarioSancionadoController {

    @Autowired
    private UsuarioSancionadoService usuarioSancionadoService;

    @GetMapping
    public ResponseEntity<List<UsuarioSancionado>> lista() {

        List<UsuarioSancionado> usuarios = usuarioSancionadoService.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<UsuarioSancionado> buscarPorRut(
            @PathVariable String rut) {

        UsuarioSancionado usuario = usuarioSancionadoService.findByRut(rut);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/motivo/{motivo}")
    public ResponseEntity<List<UsuarioSancionado>> buscarPorMotivo(
            @PathVariable String motivo) {

        List<UsuarioSancionado> usuarios =
                usuarioSancionadoService.findByMotivo(motivo);

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/fecha-sancion/{fecha}")
    public ResponseEntity<List<UsuarioSancionado>> buscarPorFechaSancion(
            @PathVariable LocalDate fecha) {

        List<UsuarioSancionado> usuarios =
                usuarioSancionadoService.findByFechaSancion(fecha);

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/fecha-expiracion/{fecha}")
    public ResponseEntity<List<UsuarioSancionado>> buscarPorFechaExpiracion(
            @PathVariable LocalDate fecha) {

        List<UsuarioSancionado> usuarios =
                usuarioSancionadoService.findByFechaExpiracion(fecha);

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioSancionado> guardar(
            @Valid @RequestBody UsuarioSancionado usuario) {

        UsuarioSancionado nuevo = usuarioSancionadoService.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSancionado> buscar(@PathVariable Long id) {

        UsuarioSancionado usuario = usuarioSancionadoService.findById(id);

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSancionado> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioSancionado usuario) {

        UsuarioSancionado actualizado =
                usuarioSancionadoService.update(id, usuario);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        usuarioSancionadoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}