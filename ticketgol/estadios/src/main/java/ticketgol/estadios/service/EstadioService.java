package ticketgol.estadios.service;

import ticketgol.estadios.model.Estadio;
import ticketgol.estadios.repository.EstadioRepository;
import ticketgol.estadios.exception.EstadioNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EstadioService {

    private static final Logger log = LoggerFactory.getLogger(EstadioService.class);

    @Autowired
    private EstadioRepository estadioRepository;

    public List<Estadio> findAll() {
        log.debug("Recuperando el catálogo completo de estadios");
        return estadioRepository.findAll();
    }

    public Estadio findById(Long id) {
        log.debug("Buscando estadio por ID: {}", id);
        return estadioRepository.findById(id).orElseThrow(() -> {
            log.error("Búsqueda fallida: No se encontró el estadio con ID: {}", id);
            return new EstadioNotFoundException("Estadio no encontrado con el ID: " + id);
        });
    }

    public Estadio save(Estadio estadio) {
        log.info("Registrando nuevo estadio en el sistema: {} ({})", estadio.getNombre(), estadio.getCiudad());
        Estadio guardado = estadioRepository.save(estadio);
        log.info("Estadio guardado exitosamente con ID asignado: {}", guardado.getId());
        return guardado;
    }

    public void delete(Long id) {
        log.info("Solicitud para eliminar el estadio ID: {}", id);
        if (!estadioRepository.existsById(id)) {
            log.error("Error al eliminar: No existe ningún estadio con ID: {}", id);
            throw new EstadioNotFoundException("No se puede eliminar. Estadio no encontrado con el ID: " + id);
        }
        estadioRepository.deleteById(id);
        log.info("Estadio ID {} eliminado correctamente de la base de datos", id);
    }

    public Estadio update(Long id, Estadio estadioDatos) {
        log.info("Solicitud para actualizar el estadio ID: {}", id);
        Estadio existente = findById(id);

        existente.setNombre(estadioDatos.getNombre());
        existente.setCiudad(estadioDatos.getCiudad());
        existente.setCapacidad(estadioDatos.getCapacidad());
        existente.setDireccion(estadioDatos.getDireccion());

        Estadio actualizado = estadioRepository.save(existente);
        log.info("Datos del estadio ID {} modificados correctamente", id);
        return actualizado;
    }
}