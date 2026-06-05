package ticketgol.compras.Service;

import org.springframework.stereotype.Service;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Repository.ComprasRepository;
import ticketgol.compras.Exception.ComprasNotFoundException; // <-- Importante: Importar la excepción

import java.util.List;

@Service
public class ComprasService {

    private final ComprasRepository repository;

    public ComprasService(ComprasRepository repository) {
        this.repository = repository;
    }

    public List<Compras> obtenerTodos() {
        return repository.findAll();
    }

    // Retorna la compra encontrada o lanza la excepción si no existe
    public Compras obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ComprasNotFoundException("La compra con ID " + id + " no existe."));
    }

    public List<Compras> obtenerPorUsuario(String usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public Compras guardar(Compras compras) {
        return repository.save(compras);
    }

    // Verifica la existencia antes de borrar para evitar errores silenciosos
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ComprasNotFoundException("No se puede eliminar. La compra con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }
}