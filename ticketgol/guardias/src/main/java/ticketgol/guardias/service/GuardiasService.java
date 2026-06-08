package ticketgol.guardias.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.guardias.model.Guardias;
import ticketgol.guardias.repository.GuardiasRepository;
import ticketgol.guardias.Exception.GuardiaNotFoundException;

import java.util.List;

@Service
public class GuardiasService {

    @Autowired
    private GuardiasRepository repository;

    public List<Guardias> obtenerTodos() {
        return repository.findAll();
    }

    public Guardias obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GuardiaNotFoundException("El guardia con ID " + id + " no existe."));
    }

    public Guardias guardar(Guardias guardia) {
        return repository.save(guardia);
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new GuardiaNotFoundException("No se puede eliminar. El guardia con ID " + id + " no existe.");
        }
        repository.deleteById(id);
    }

    public List<Guardias> obtenerPorEstadio(Long estadioId) {
        return repository.findByEstadioId(estadioId);
    }
}