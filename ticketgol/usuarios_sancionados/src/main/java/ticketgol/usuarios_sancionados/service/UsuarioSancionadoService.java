package ticketgol.usuarios_sancionados.service;

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
                .orElseThrow(() -> new RuntimeException("Usuario sancionado no encontrado"));
    }

    public UsuarioSancionado save(UsuarioSancionado usuarioSancionado) {
        return usuarioSancionadoRepository.save(usuarioSancionado);
    }

    public void delete(Long id) {
        usuarioSancionadoRepository.deleteById(id);
    }
}