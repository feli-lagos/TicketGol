package ticketgol.usuarios_sancionados.controller;

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
public class UsuarioSancionadoController {

    @Autowired
    private UsuarioSancionadoService usuarioSancionadoService;

    @GetMapping
    public Mono<ResponseEntity<List<UsuarioSancionado>>> lista() {
        return Mono.fromCallable(() -> usuarioSancionadoService.findAll())
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @GetMapping("/rut/{rut}")
    public Mono<ResponseEntity<UsuarioSancionado>> buscarPorRut(@PathVariable String rut) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByRut(rut))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/motivo/{motivo}")
    public Mono<ResponseEntity<List<UsuarioSancionado>>> buscarPorMotivo(@PathVariable String motivo) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByMotivo(motivo))
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @GetMapping("/fecha-sancion/{fecha}")
    public Mono<ResponseEntity<List<UsuarioSancionado>>> buscarPorFechaSancion(@PathVariable LocalDate fecha) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByFechaSancion(fecha))
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @GetMapping("/fecha-expiracion/{fecha}")
    public Mono<ResponseEntity<List<UsuarioSancionado>>> buscarPorFechaExpiracion(@PathVariable LocalDate fecha) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findByFechaExpiracion(fecha))
                .subscribeOn(Schedulers.boundedElastic())
                .map(usuarios -> usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios));
    }

    @PostMapping
    public Mono<ResponseEntity<UsuarioSancionado>> guardar(@Valid @RequestBody UsuarioSancionado usuario) {
        return Mono.fromCallable(() -> usuarioSancionadoService.save(usuario))
                .subscribeOn(Schedulers.boundedElastic())
                .map(nuevo -> ResponseEntity.status(HttpStatus.CREATED).body(nuevo));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UsuarioSancionado>> buscar(@PathVariable Long id) {
        return Mono.fromCallable(() -> usuarioSancionadoService.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UsuarioSancionado>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioSancionado usuario) {
        return Mono.fromCallable(() -> usuarioSancionadoService.update(id, usuario))
                .subscribeOn(Schedulers.boundedElastic())
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> eliminar(@PathVariable Long id) {
        return Mono.<Void>fromRunnable(() -> usuarioSancionadoService.delete(id))
                .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}