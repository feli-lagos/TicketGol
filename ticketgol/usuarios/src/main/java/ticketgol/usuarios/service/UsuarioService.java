package ticketgol.usuarios.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ticketgol.usuarios.dto.UsuarioSancionadoDTO;
import ticketgol.usuarios.model.Usuario;
import ticketgol.usuarios.repository.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String sancionadosUrl = "http://localhost:8081/api/v1/usuarios-sancionados";

    private void chequeoSancion(Usuario usuario) {
        try {
            String url = sancionadosUrl + "/rut/" + usuario.getRut();
            log.debug("Consultando estado de sanción para RUT: {}", usuario.getRut());
            ResponseEntity<UsuarioSancionadoDTO> response = restTemplate.getForEntity(url, UsuarioSancionadoDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                UsuarioSancionadoDTO sancion = response.getBody();
                if (LocalDate.now().isBefore(sancion.getFechaExpiracion().plusDays(1))) {
                    usuario.setEstadoSancion("SANCIONADO");
                    log.warn("Usuario RUT {} detectado como SANCIONADO hasta el {}", usuario.getRut(), sancion.getFechaExpiracion());
                } else {
                    usuario.setEstadoSancion("ACTIVO");
                }
            } else {
                usuario.setEstadoSancion("ACTIVO");
            }
        } catch (HttpClientErrorException.NotFound e) {
            usuario.setEstadoSancion("ACTIVO");
            log.info("El usuario RUT {} no registra sanciones activas en el sistema remoto", usuario.getRut());
        } catch (Exception e) {
            usuario.setEstadoSancion("ERROR_DESCONOCIDO");
            log.error("Falla de comunicación con el servicio de sanciones para el RUT {}: {}", usuario.getRut(), e.getMessage());
        }
    }

    public Usuario save(Usuario usuario) {
        log.info("Solicitud para registrar nuevo usuario. RUT: {}, Correo: [{}]", usuario.getRut(), usuario.getCorreo());

        if (usuarioRepository.existsByRut(usuario.getRut())) {
            log.warn("Registro rechazado: El RUT {} ya existe en la base de datos", usuario.getRut());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUT ya se encuentra registrado.");
        }

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            log.warn("Registro rechazado: El correo [{}] ya está en uso", usuario.getCorreo());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya se encuentra registrado.");
        }

        Usuario usuarioCreado = usuarioRepository.save(usuario);
        chequeoSancion(usuarioCreado);

        log.info("Usuario creado exitosamente. ID generado: {}, Estado Inicial: [{}]", usuarioCreado.getId(), usuarioCreado.getEstadoSancion());
        return usuarioCreado;
    }

    public List<Usuario> findAll() {
        log.debug("Recuperando listado completo de usuarios globales");
        List<Usuario> usuarios = usuarioRepository.findAll();

        log.info("Procesando estado de sanciones para {} usuarios encontrados", usuarios.size());
        for (Usuario usuario : usuarios) {
            chequeoSancion(usuario);
        }
        return usuarios;
    }

    public Usuario findById(Long id) {
        log.debug("Buscando usuario por ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
<<<<<<< HEAD
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        checkSanctionStatus(usuario);
=======
                .orElseThrow(() -> {
                    log.error("Búsqueda fallida: No se encontró ningún usuario con ID: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
                });

        chequeoSancion(usuario);
>>>>>>> main
        return usuario;
    }

    public Usuario findByRut(String rut) {
        log.debug("Buscando usuario por RUT: {}", rut);
        Usuario usuario = usuarioRepository.findByRut(rut)
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: No existe usuario asociado al RUT: {}", rut);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con RUT: " + rut);
                });

        chequeoSancion(usuario);
        return usuario;
    }

    public Usuario findByCorreo(String correo) {
        log.debug("Buscando usuario por Correo: [{}]", correo);
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> {
                    log.warn("Búsqueda fallida: No existe usuario con correo: [{}]", correo);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con correo: " + correo);
                });

        chequeoSancion(usuario);
        return usuario;
    }

    public Usuario update(Long id, Usuario nuevosDatos) {
        log.info("Solicitud para actualizar datos del usuario ID: {}", id);
        Usuario existente = findById(id);

        usuarioRepository.findByRut(nuevosDatos.getRut()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                log.warn("Actualización denegada: El RUT {} ya le pertenece al usuario ID: {}", nuevosDatos.getRut(), u.getId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUT ya pertenece a otro usuario.");
            }
        });

        usuarioRepository.findByCorreo(nuevosDatos.getCorreo()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                log.warn("Actualización denegada: El correo [{}] ya le pertenece al usuario ID: {}", nuevosDatos.getCorreo(), u.getId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya pertenece a otro usuario.");
            }
        });

        existente.setRut(nuevosDatos.getRut());
        existente.setCorreo(nuevosDatos.getCorreo());
        existente.setNombreVisible(nuevosDatos.getNombreVisible());
        existente.setTelefono(nuevosDatos.getTelefono());
        existente.setHashClave(nuevosDatos.getHashClave());

        Usuario usuarioActualizado = usuarioRepository.save(existente);
        chequeoSancion(usuarioActualizado);

        log.info("Usuario ID {} modificado correctamente en el sistema", id);
        return usuarioActualizado;
    }

    public void delete(Long id) {
        log.info("Solicitud para eliminar el usuario ID: {}", id);

        if (!usuarioRepository.existsById(id)) {
            log.error("Error al eliminar: No existe el usuario con ID: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
        log.info("Usuario ID {} eliminado permanentemente de la base de datos", id);
    }
}