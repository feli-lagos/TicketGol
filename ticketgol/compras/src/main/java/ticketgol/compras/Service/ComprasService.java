package ticketgol.compras.Service;

import org.springframework.stereotype.Service;
import ticketgol.compras.Model.Compras;
import ticketgol.compras.Repository.ComprasRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ComprasService {

    private final ComprasRepository repository;

    public ComprasService(ComprasRepository repository) {
        this.repository = repository;
    }
    public List<Compras> obtenerTodos() {
        return repository.findAll();
    }
    public Optional<Compras> obtenerPorId(Long id) {
        return repository.findById(id);
    }
    public List<Compras> obtenerPorUsuario(String usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
    public Compras guardar(Compras compras) {
        return repository.save(compras);
    }
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}