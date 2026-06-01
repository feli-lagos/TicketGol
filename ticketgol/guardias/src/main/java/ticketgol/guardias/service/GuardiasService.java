package ticketgol.guardias.service;

import org.springframework.stereotype.Service;
import ticketgol.guardias.model.Guardias;
import ticketgol.guardias.repository.GuardiasRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GuardiasService {
    private final GuardiasRepository repository;

    public GuardiasService(GuardiasRepository repository) {
        this.repository = repository;
    }

    public List<Guardias> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Guardias> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public Guardias guardar(Guardias guardia) {
        return repository.save(guardia);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // buscar guardias  con estadio
    public List<Guardias> obtenerPorEstadio(Long estadioId) {
        return repository.findByEstadioId(estadioId);
    }
}

