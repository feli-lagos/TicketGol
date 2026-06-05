package ticketgol.usuarios.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.usuarios.exception.UsuarioAlreadyExistsException;
import ticketgol.usuarios.exception.UsuarioNotFoundException;
import ticketgol.usuarios.model.Usuario;
import ticketgol.usuarios.repository.UsuarioRepository;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException(
                                "Usuario con ID " + id + " no encontrado"
                        )
                );
    }

    public Usuario save(Usuario usuario) {

        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario con el RUT " + usuario.getRut()
            );
        }

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario con el correo " + usuario.getCorreo()
            );
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuarioActualizado) {

        Usuario usuarioExistente = findById(id);

        if (!usuarioExistente.getRut().equals(usuarioActualizado.getRut())
                && usuarioRepository.existsByRut(usuarioActualizado.getRut())) {

            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario con el RUT " +
                            usuarioActualizado.getRut()
            );
        }

        if (!usuarioExistente.getCorreo().equals(usuarioActualizado.getCorreo())
                && usuarioRepository.existsByCorreo(usuarioActualizado.getCorreo())) {

            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario con el correo " +
                            usuarioActualizado.getCorreo()
            );
        }

        usuarioExistente.setRut(usuarioActualizado.getRut());
        usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        usuarioExistente.setNombreVisible(usuarioActualizado.getNombreVisible());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setHashClave(usuarioActualizado.getHashClave());

        return usuarioRepository.save(usuarioExistente);
    }

    public void delete(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(
                    "Usuario con ID " + id + " no encontrado"
            );
        }

        usuarioRepository.deleteById(id);
    }
}