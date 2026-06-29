package ticketgol.usuarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.usuarios.model.Usuario;
import jakarta.validation.Valid;
import ticketgol.usuarios.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Todas las operaciones de gestión de usuarios del sistema.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Obtiene una lista completa de todos los usuarios registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada con éxito"),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados en el sistema")
    })
    public ResponseEntity<List<Usuario>> lista() {
        List<Usuario> usuarios = usuarioService.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar usuario por RUT", description = "Busca los detalles de un usuario utilizando su identificador de RUT único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario con el RUT especificado no encontrado")
    })
    public ResponseEntity<Usuario> buscarPorRut(@PathVariable String rut) {
        Usuario usuario = usuarioService.findByRut(rut);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/correo/{correo}")
    @Operation(summary = "Buscar usuario por correo", description = "Busca los detalles de un usuario utilizando su dirección de correo electrónico institucional o personal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario con el correo especificado no encontrado")
    })
    public ResponseEntity<Usuario> buscarPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.findByCorreo(correo);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo registro de usuario en el sistema con sus datos correspondientes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o mal estructurados")
    })
    public ResponseEntity<Usuario> guardar(@Valid @RequestBody Usuario usuario) {
        Usuario nuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Busca los detalles de un usuario utilizando su identificador numérico interno (ID).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "ID de usuario no encontrado")
    })
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Modifica los datos de un usuario existente buscando por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado para actualizar"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.update(id, usuario);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Remueve definitivamente el registro de un usuario del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado para eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}