package ticketgol.usuarios.service;

import jakarta.transaction.Transactional;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String sancionadosUrl = "http://localhost:8081/api/v1/usuarios-sancionados";

    private void checkSanctionStatus(Usuario usuario) {
        try {
            String url = sancionadosUrl + "/rut/" + usuario.getRut();
            ResponseEntity<UsuarioSancionadoDTO> response = restTemplate.getForEntity(url, UsuarioSancionadoDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                UsuarioSancionadoDTO sancion = response.getBody();
                if (LocalDate.now().isBefore(sancion.getFechaExpiracion().plusDays(1))) {
                    usuario.setEstadoSancion("SANCIONADO");
                } else {
                    usuario.setEstadoSancion("ACTIVO");
                }
            } else {
                usuario.setEstadoSancion("ACTIVO");
            }
        } catch (HttpClientErrorException.NotFound e) {
            usuario.setEstadoSancion("ACTIVO");
        } catch (Exception e) {
            usuario.setEstadoSancion("UNKNOWN_SERVICE_ERROR");
        }
    }

    public Usuario save(Usuario usuario) {
        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUT ya se encuentra registrado.");
        }

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya se encuentra registrado.");
        }

        Usuario usuarioCreado = usuarioRepository.save(usuario);
        checkSanctionStatus(usuarioCreado);
        return usuarioCreado;
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            checkSanctionStatus(usuario);
        }
        return usuarios;
    }

    public Usuario findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        checkSanctionStatus(usuario);
        return usuario;
    }

    public Usuario findByRut(String rut) {
        Usuario usuario = usuarioRepository.findByRut(rut)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con RUT: " + rut));

        checkSanctionStatus(usuario);
        return usuario;
    }

    public Usuario findByCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con correo: " + correo));

        checkSanctionStatus(usuario);
        return usuario;
    }

    public Usuario update(Long id, Usuario nuevosDatos) {
        Usuario existente = findById(id);

        usuarioRepository.findByRut(nuevosDatos.getRut()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El RUT ya pertenece a otro usuario.");
            }
        });

        usuarioRepository.findByCorreo(nuevosDatos.getCorreo()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya pertenece a otro usuario.");
            }
        });

        existente.setRut(nuevosDatos.getRut());
        existente.setCorreo(nuevosDatos.getCorreo());
        existente.setNombreVisible(nuevosDatos.getNombreVisible());
        existente.setTelefono(nuevosDatos.getTelefono());
        existente.setHashClave(nuevosDatos.getHashClave());

        Usuario usuarioActualizado = usuarioRepository.save(existente);
        checkSanctionStatus(usuarioActualizado);
        return usuarioActualizado;
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}