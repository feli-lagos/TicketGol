package ticketgol.usuarios_sancionados.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import ticketgol.usuarios_sancionados.service.UsuarioSancionadoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios-sancionados")
@Tag(name = "Usuarios Sancionados", description = "Todas las operaciones de usuarios sancionados.")
public class UsuarioSancionadoController {

    @Autowired
    private UsuarioSancionadoService usuarioSancionadoService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios sancionados", description = "Obtiene una lista completa de todos los usuarios registrados con sanciones en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios sancionados recuperada con éxito"),
            @ApiResponse(responseCode = "244", description = "No hay usuarios sancionados registrados")
    })
    public Mono<ResponseEntity<List<UsuarioSancionado>>> lista() {
        return Mono.fromCallable(() -> usuarioSancionadoService.findAll())
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar usuario sancionado por RUT", description = "Busca los detalles de la sanción de un usuario utilizando su identificador de RUT único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario sancionado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSancionado.class))),
            @ApiResponse(responseCode = "404", description = "Usuario con el RUT especificado no encontrado")
    })
    public Mono<ResponseEntity<UsuarioSancionado>> buscarPorRut(@PathVariable String rut) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByRut(rut))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/motivo/{motivo}")
    @Operation(summary = "Buscar usuarios por motivo de sanción", description = "Obtiene una lista de usuarios filtrados por el tipo o motivo específico de su sanción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios filtrados por motivo recuperados con éxito"),
            @ApiResponse(responseCode = "244", description = "No se encontraron usuarios con ese motivo de sanción")
    })
    public Mono<ResponseEntity<List<UsuarioSancionado>>> buscarPorMotivo(@PathVariable String motivo) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByMotivo(motivo))
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @GetMapping("/fecha-sancion/{fecha}")
    @Operation(summary = "Buscar usuarios por fecha de sanción", description = "Obtiene una lista de usuarios que recibieron una sanción en una fecha específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios sancionados en la fecha especificada recuperados"),
            @ApiResponse(responseCode = "244", description = "No hay registros de sanciones para esa fecha")
    })
    public Mono<ResponseEntity<List<UsuarioSancionado>>> buscarPorFechaSancion(@PathVariable LocalDate fecha) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByFechaSancion(fecha))
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @GetMapping("/fecha-expiracion/{fecha}")
    @Operation(summary = "Buscar usuarios por fecha de expiración", description = "Obtiene una lista de usuarios cuya sanción expira en la fecha indicada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios cuya sanción expira en la fecha especificada recuperados"),
            @ApiResponse(responseCode = "244", description = "No hay registros de expiración para esa fecha")
    })
    public Mono<ResponseEntity<List<UsuarioSancionado>>> buscarPorFechaExpiracion(@PathVariable LocalDate fecha) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByFechaExpiracion(fecha))
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo usuario sancionado", description = "Crea un nuevo registro de sanción en el sistema para un usuario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario sancionado registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSancionado.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o mal estructurados")
    })
    public Mono<ResponseEntity<UsuarioSancionado>> guardar(@Valid @RequestBody UsuarioSancionado usuario) {
        return Mono.fromCallable(() -> usuarioSancionadoService.save(usuario))
                .subscribeOn(Schedulers.boundedElastic())
                .map(nuevo -> ResponseEntity.status(HttpStatus.CREATED).body(nuevo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario sancionado por ID", description = "Busca los detalles de un registro de sanción utilizando su identificador numérico interno (ID).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de sanción encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSancionado.class))),
            @ApiResponse(responseCode = "404", description = "ID de sanción no encontrado")
    })
    public Mono<ResponseEntity<UsuarioSancionado>> buscar(@PathVariable Long id) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario sancionado", description = "Modifica los datos o vigencia de un registro de sanción existente buscando por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de sanción actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSancionado.class))),
            @ApiResponse(responseCode = "404", description = "Registro de sanción no encontrado para actualizar"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    public Mono<ResponseEntity<UsuarioSancionado>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioSancionado usuario) {
        return Mono.fromCallable(() -> usuarioSancionadoService.update(id, usuario))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sanción de usuario", description = "Remueve definitivamente el registro de sanción del sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sanción eliminada exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Registro de sanción no encontrado para eliminar")
    })
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id) {
        return Mono.<Void>fromRunnable(() -> usuarioSancionadoService.delete(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}