package ticketgol.usuarios_sancionados.service;

import ticketgol.usuarios_sancionados.exception.UsuarioAlreadyExistsException;
import ticketgol.usuarios_sancionados.exception.UsuarioNotFoundException;
import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import ticketgol.usuarios_sancionados.repository.UsuarioSancionadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UsuarioSancionadoService {

    @Autowired
    private UsuarioSancionadoRepository usuarioSancionadoRepository;

    public List<UsuarioSancionado> findAll() {
        return usuarioSancionadoRepository.findAll();
    }

    public UsuarioSancionado findById(Long id) {
        return usuarioSancionadoRepository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException(
                                "Usuario sancionado con ID " + id + " no encontrado"
                        )
                );
    }

    public UsuarioSancionado save(UsuarioSancionado usuarioSancionado) {

        if (usuarioSancionadoRepository.existsByRut(usuarioSancionado.getRut())) {
            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario sancionado con RUT " +
                            usuarioSancionado.getRut()
            );
        }

        return usuarioSancionadoRepository.save(usuarioSancionado);
    }

    public UsuarioSancionado update(Long id, UsuarioSancionado usuarioActualizado) {

        UsuarioSancionado usuarioExistente = findById(id);

        usuarioExistente.setRut(usuarioActualizado.getRut());
        usuarioExistente.setMotivo(usuarioActualizado.getMotivo());
        usuarioExistente.setFechaSancion(usuarioActualizado.getFechaSancion());
        usuarioExistente.setFechaExpiracion(usuarioActualizado.getFechaExpiracion());

        return usuarioSancionadoRepository.save(usuarioExistente);
    }

    public void delete(Long id) {

        if (!usuarioSancionadoRepository.existsById(id)) {
            throw new UsuarioNotFoundException(
                    "Usuario sancionado con ID " + id + " no encontrado"
            );
        }

        usuarioSancionadoRepository.deleteById(id);
    }
}