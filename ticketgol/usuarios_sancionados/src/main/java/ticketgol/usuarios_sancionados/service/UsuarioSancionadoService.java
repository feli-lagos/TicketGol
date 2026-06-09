package ticketgol.usuarios_sancionados.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ticketgol.usuarios_sancionados.dto.UsuarioDTO;
import ticketgol.usuarios_sancionados.exception.UsuarioAlreadyExistsException;
import ticketgol.usuarios_sancionados.exception.UsuarioNotFoundException;
import ticketgol.usuarios_sancionados.exception.UsuarioSancionadoNotFoundException;
import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import ticketgol.usuarios_sancionados.repository.UsuarioSancionadoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class UsuarioSancionadoService {

    @Autowired
    private UsuarioSancionadoRepository usuarioSancionadoRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/api/v1/usuarios")
            .build();

    private Mono<UsuarioDTO> validarUsuarioExternoPorRut(String rut) {
        return webClient.get()
                .uri("/rut/" + rut)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new UsuarioNotFoundException("Validación fallida: No se puede procesar el registro porque el usuario con RUT " + rut + " no existe en el sistema."))
                )
                .bodyToMono(UsuarioDTO.class)
                .onErrorMap(WebClientResponseException.class, e -> e.getStatusCode().value() == 404
                        ? new UsuarioNotFoundException("Validación fallida: No se puede procesar el registro porque el usuario con RUT " + rut + " no existe en el sistema.")
                        : new RuntimeException("Error en la pasarela de comunicación con el microservicio de Usuarios: " + e.getMessage())
                );
    }

    public List<UsuarioSancionado> findAll() {
        return usuarioSancionadoRepository.findAll();
    }

    public UsuarioSancionado findById(Long id) {
        return usuarioSancionadoRepository.findById(id)
                .orElseThrow(() -> new UsuarioSancionadoNotFoundException(
                        "Usuario sancionado con ID " + id + " no encontrado"
                ));
    }

    public UsuarioSancionado findByRut(String rut) {
        UsuarioSancionado usuario = usuarioSancionadoRepository.findByRut(rut);
        if (usuario == null) {
            throw new UsuarioSancionadoNotFoundException(
                    "Usuario sancionado con RUT " + rut + " no encontrado"
            );
        }
        return usuario;
    }

    public List<UsuarioSancionado> findByMotivo(String motivo) {
        return usuarioSancionadoRepository.findByMotivo(motivo);
    }

    public List<UsuarioSancionado> findByFechaSancion(LocalDate fechaSancion) {
        return usuarioSancionadoRepository.findByFechaSancion(fechaSancion);
    }

    public List<UsuarioSancionado> findByFechaExpiracion(LocalDate fechaExpiracion) {
        return usuarioSancionadoRepository.findByFechaExpiracion(fechaExpiracion);
    }

    public UsuarioSancionado save(UsuarioSancionado usuarioSancionado) {
        validarUsuarioExternoPorRut(usuarioSancionado.getRut()).block();

        if (usuarioSancionadoRepository.existsByRut(usuarioSancionado.getRut())) {
            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario sancionado con RUT " + usuarioSancionado.getRut()
            );
        }

        return usuarioSancionadoRepository.save(usuarioSancionado);
    }

    public UsuarioSancionado update(Long id, UsuarioSancionado usuarioActualizado) {
        UsuarioSancionado usuarioExistente = findById(id);

        validarUsuarioExternoPorRut(usuarioActualizado.getRut()).block();

        if (!usuarioExistente.getRut().equals(usuarioActualizado.getRut())
                && usuarioSancionadoRepository.existsByRut(usuarioActualizado.getRut())) {
            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario sancionado con RUT " + usuarioActualizado.getRut()
            );
        }

        usuarioExistente.setRut(usuarioActualizado.getRut());
        usuarioExistente.setMotivo(usuarioActualizado.getMotivo());
        usuarioExistente.setFechaSancion(usuarioActualizado.getFechaSancion());
        usuarioExistente.setFechaExpiracion(usuarioActualizado.getFechaExpiracion());

        return usuarioSancionadoRepository.save(usuarioExistente);
    }

    public void delete(Long id) {
        if (!usuarioSancionadoRepository.existsById(id)) {
            throw new UsuarioSancionadoNotFoundException(
                    "Usuario sancionado con ID " + id + " no encontrado"
            );
        }
        usuarioSancionadoRepository.deleteById(id);
    }
}