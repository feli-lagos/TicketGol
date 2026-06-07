package ticketgol.compras.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Repository.ComprasRepository;
import ticketgol.compras.Exception.ComprasNotFoundException;

import java.util.List;

@Service
public class ComprasService {

    @Autowired
    private ComprasRepository repositorio;

    public List<Compras> obtenerTodos() {
        return repositorio.findAll();
    }

    public Compras obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ComprasNotFoundException("La compra con ID " + id + " no existe."));
    }

    public Compras guardar(Compras compras) {
        return repositorio.save(compras);
    }

    public List<Compras> obtenerPorUsuario(String usuarioId) {
        return repositorio.findByUsuarioId(usuarioId);
    }

    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ComprasNotFoundException("No se puede eliminar. La compra con ID " + id + " no existe.");
        }
        repositorio.deleteById(id);
    }
}