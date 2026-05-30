package ticketgol.usuarios_sancionados.controller;

import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import ticketgol.usuarios_sancionados.service.UsuarioSancionadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<UsuarioSancionado> guardar(@RequestBody UsuarioSancionado usuario) {
        UsuarioSancionado nuevo = usuarioSancionadoService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSancionado> buscar(@PathVariable Long id) {
        try {
            UsuarioSancionado usuario = usuarioSancionadoService.findById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSancionado> actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioSancionado usuario) {

        try {
            UsuarioSancionado z = usuarioSancionadoService.findById(id);

            z.setId(id);
            z.setRut(usuario.getRut());
            z.setMotivo(usuario.getMotivo());
            z.setFechaSancion(usuario.getFechaSancion());
            z.setFechaExpiracion(usuario.getFechaExpiracion());

            usuarioSancionadoService.save(z);

            return ResponseEntity.ok(z);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioSancionadoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}