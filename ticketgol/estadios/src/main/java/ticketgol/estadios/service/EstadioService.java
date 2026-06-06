package ticketgol.estadios.service;

import ticketgol.estadios.model.Estadio;
import ticketgol.estadios.repository.EstadioRepository;
import ticketgol.estadios.exception.EstadioNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public List<Estadio> findAll() {
        return estadioRepository.findAll();
    }

    public Estadio findById(Long id) {
        // Replaced RuntimeException with your custom exception so the handler can catch it
        return estadioRepository.findById(id).orElseThrow(()
                -> new EstadioNotFoundException("Estadio no encontrado con el ID: " + id));
    }

    public List<Estadio> findByCiudad(String ciudad) {
        return estadioRepository.findByCiudad(ciudad);
    }

    public List<Estadio> findByNombre(String nombre) {
        return estadioRepository.findByNombre(nombre);
    }

    public Estadio save(Estadio estadio) {
        return estadioRepository.save(estadio);
    }

    public void delete(Long id) {
        if (!estadioRepository.existsById(id)) {
            throw new EstadioNotFoundException("No se puede eliminar. Estadio no encontrado con el ID: " + id);
        }
        estadioRepository.deleteById(id);
    }
}