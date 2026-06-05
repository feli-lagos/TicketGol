package ticketgol.usuarios_sancionados.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import ticketgol.usuarios_sancionados.service.UsuarioSancionadoService;

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
    public ResponseEntity<UsuarioSancionado> guardar(
            @RequestBody UsuarioSancionado usuario) {

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
            @RequestBody UsuarioSancionado usuario) {

        UsuarioSancionado actualizado =
                usuarioSancionadoService.update(id, usuario);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        usuarioSancionadoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}