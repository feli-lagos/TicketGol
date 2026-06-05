package ticketgol.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.usuarios.model.Usuario;
import ticketgol.usuarios.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> lista() {

        List<Usuario> usuarios = usuarioService.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {

        Usuario nuevo = usuarioService.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {

        Usuario usuario = usuarioService.findById(id);

        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Usuario actualizado = usuarioService.update(id, usuario);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        usuarioService.delete(id);

        return ResponseEntity.noContent().build();
    }
}