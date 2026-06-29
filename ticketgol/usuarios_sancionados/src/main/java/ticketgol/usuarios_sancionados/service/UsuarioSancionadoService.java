package ticketgol.usuarios_sancionados.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UsuarioSancionadoService.class);

    @Autowired
    private UsuarioSancionadoRepository usuarioSancionadoRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8082/api/v1/usuarios")
            .build();

    private Mono<UsuarioDTO> validarUsuarioExternoPorRut(String rut) {
        log.debug("Iniciando validación remota en servicio de usuarios para el RUT: {}", rut);
        return webClient.get()
                .uri("/rut/" + rut)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.warn("El servicio remoto retornó un error 4xx para el RUT: {}", rut);
                    return Mono.error(new UsuarioNotFoundException("Validación fallida: No se puede procesar el registro porque el usuario con RUT " + rut + " no existe en el sistema."));
                })
                .bodyToMono(UsuarioDTO.class)
                .onErrorMap(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().value() == 404) {
                        log.warn("Validación fallida: El RUT {} no existe en el sistema de usuarios.", rut);
                        return new UsuarioNotFoundException("Validación fallida: No se puede procesar el registro porque el usuario con RUT " + rut + " no existe en el sistema.");
                    }
                    log.error("Falla crítica en la comunicación de Usuarios: {}", e.getMessage());
                    return new RuntimeException("Error en encontrar el usuario u otro: " + e.getMessage());
                });
    }

    public List<UsuarioSancionado> findAll() {
        log.debug("Recuperando listado completo de usuarios sancionados");
        return usuarioSancionadoRepository.findAll();
    }

    public UsuarioSancionado findById(Long id) {
        log.debug("Buscando registro de sanción por ID: {}", id);
        return usuarioSancionadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Búsqueda fallida: No existe registro de sanción con ID: {}", id);
                    return new UsuarioSancionadoNotFoundException("Usuario sancionado con ID " + id + " no encontrado");
                });
    }

    public UsuarioSancionado findByRut(String rut) {
        log.debug("Buscando registro de sanción por RUT: {}", rut);
        UsuarioSancionado usuario = usuarioSancionadoRepository.findByRut(rut);
        if (usuario == null) {
            log.warn("Búsqueda vacía: El RUT {} no registra ninguna sanción en el sistema", rut);
            throw new UsuarioSancionadoNotFoundException("Usuario sancionado con RUT " + rut + " no encontrado");
        }
        return usuario;
    }

    public List<UsuarioSancionado> findByMotivo(String motivo) {
        log.debug("Buscando sanciones asociadas al motivo: [{}]", motivo);
        return usuarioSancionadoRepository.findByMotivo(motivo);
    }

    public List<UsuarioSancionado> findByFechaSancion(LocalDate fechaSancion) {
        log.debug("Filtrando sanciones aplicadas en la fecha: {}", fechaSancion);
        return usuarioSancionadoRepository.findByFechaSancion(fechaSancion);
    }

    public List<UsuarioSancionado> findByFechaExpiracion(LocalDate fechaExpiracion) {
        log.debug("Filtrando sanciones que expiran en la fecha: {}", fechaExpiracion);
        return usuarioSancionadoRepository.findByFechaExpiracion(fechaExpiracion);
    }

    public UsuarioSancionado save(UsuarioSancionado usuarioSancionado) {
        log.info("Solicitud para registrar nueva sanción para el RUT: {}", usuarioSancionado.getRut());
        validarUsuarioExternoPorRut(usuarioSancionado.getRut()).block();

        if (usuarioSancionadoRepository.existsByRut(usuarioSancionado.getRut())) {
            log.warn("Registro duplicado: El RUT {} ya cuenta con una sanción en el sistema", usuarioSancionado.getRut());
            throw new UsuarioAlreadyExistsException("Ya existe un usuario sancionado con RUT " + usuarioSancionado.getRut());
        }

        UsuarioSancionado guardado = usuarioSancionadoRepository.save(usuarioSancionado);
        log.info("Sanción registrada correctamente con ID: {} para el usuario RUT: {}", guardado.getId(), guardado.getRut());
        return guardado;
    }

    public UsuarioSancionado update(Long id, UsuarioSancionado usuarioActualizado) {
        log.info("Solicitud para modificar registro de sanción ID: {}", id);
        UsuarioSancionado usuarioExistente = findById(id);

        validarUsuarioExternoPorRut(usuarioActualizado.getRut()).block();

        if (!usuarioExistente.getRut().equals(usuarioActualizado.getRut())
                && usuarioSancionadoRepository.existsByRut(usuarioActualizado.getRut())) {
            log.warn("Conflicto de actualización: El RUT {} ya está sancionado en otro registro diferente", usuarioActualizado.getRut());
            throw new UsuarioAlreadyExistsException("Ya existe un usuario sancionado con RUT " + usuarioActualizado.getRut());
        }

        usuarioExistente.setRut(usuarioActualizado.getRut());
        usuarioExistente.setMotivo(usuarioActualizado.getMotivo());
        usuarioExistente.setFechaSancion(usuarioActualizado.getFechaSancion());
        usuarioExistente.setFechaExpiracion(usuarioActualizado.getFechaExpiracion());

        UsuarioSancionado modificado = usuarioSancionadoRepository.save(usuarioExistente);
        log.info("Registro de sanción ID {} modificado exitosamente", id);
        return modificado;
    }

    public void delete(Long id) {
        log.info("Solicitud para eliminar la sanción ID: {}", id);
        if (!usuarioSancionadoRepository.existsById(id)) {
            log.error("Error al eliminar: No se encontró la sanción con ID: {}", id);
            throw new UsuarioSancionadoNotFoundException("Usuario sancionado con ID " + id + " no encontrado");
        }
        usuarioSancionadoRepository.deleteById(id);
        log.info("Registro de sanción ID {} eliminado permanentemente", id);
    }
}