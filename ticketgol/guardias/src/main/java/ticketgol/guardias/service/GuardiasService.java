package ticketgol.guardias.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.guardias.model.Guardias;
import ticketgol.guardias.repository.GuardiasRepository;
import ticketgol.guardias.Exception.GuardiaNotFoundException;

import java.util.List;

@Slf4j
@Service
public class GuardiasService {

    @Autowired
    private GuardiasRepository repository;

    public List<Guardias> obtenerTodos() {
        log.info("Buscando la lista completa de guardias en el sistema.");
        return repository.findAll();
    }

    public Guardias obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    // Log de advertencia si se busca un ID de guardia inexistente
                    log.warn("No se pudo encontrar al guardia. El ID: " + id + " no existe en la base de datos.");
                    return new GuardiaNotFoundException("El guardia con ID " + id + " no existe.");
                });
    }

    public Guardias guardar(Guardias guardia) {
        Guardias guardiaGuardado = repository.save(guardia);
        log.info("Guardia registrado/actualizado con éxito. ID Asignado: " + guardiaGuardado.getId() +
                " | Nombre: " + guardiaGuardado.getNombre() +
                " | Asignado al Estadio ID: " + guardiaGuardado.getEstadioId());

        return guardiaGuardado;
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            log.error("Fallo al eliminar. No existe ningún guardia registrado con el ID: " + id);
            throw new GuardiaNotFoundException("No se puede eliminar. El guardia con ID " + id + " no existe.");
        }

        repository.deleteById(id);
        log.info("El guardia con ID: " + id + " ha sido desvinculado y eliminado correctamente del sistema.");
    }

    public List<Guardias> obtenerPorEstadio(Long estadioId) {
        log.info("Consultando la nómina de guardias asignados al Estadio con ID: " + estadioId);
        return repository.findByEstadioId(estadioId);
    }
}