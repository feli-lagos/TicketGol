package ticketgol.compras.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Repository.ComprasRepository;
import ticketgol.compras.Exception.ComprasNotFoundException;

import java.util.List;

@Slf4j // para activar log
@Service
public class ComprasService {

    @Autowired
    private ComprasRepository repositorio;

    public List<Compras> obtenerTodos() {
        log.info("Solicitando la lista de todas las compras realizadas.");
        return repositorio.findAll();
    }

    public Compras obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> {

                    log.warn("Intento fallido de buscar compra. La compra con ID: " + id + " no existe.");
                    return new ComprasNotFoundException("La compra con ID " + id + " no existe.");
                });
    }

    public Compras guardar(Compras compras) {
        Compras compraGuardada = repositorio.save(compras);
        log.info("Compra creada con éxito. ID de la compra: " + compraGuardada.getId() + " para el usuario: " + compraGuardada.getUsuarioId());
        return compraGuardada;
    }

    public List<Compras> obtenerPorUsuario(String usuarioId) {
        log.info("Buscando el historial de compras para el usuario con ID: " + usuarioId);
        return repositorio.findByUsuarioId(usuarioId);
    }

    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            log.error("Error al intentar eliminar. No existe ninguna compra con el ID: " + id);
            throw new ComprasNotFoundException("No se puede eliminar. La compra con ID " + id + " no existe.");
        }

        repositorio.deleteById(id);
        log.info("Compra con ID: " + id + " fue eliminada correctamente del sistema.");
    }
}