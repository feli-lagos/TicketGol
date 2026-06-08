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
        return estadioRepository.findById(id).orElseThrow(()
                -> new EstadioNotFoundException("Estadio no encontrado con el ID: " + id));
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

    // agregué este method de los últimos porque me tiraba errores en el estadio secciones
    public Estadio update(Long id, Estadio estadioDatos) {
        Estadio existente = findById(id);
        existente.setNombre(estadioDatos.getNombre());
        existente.setCiudad(estadioDatos.getCiudad());
        existente.setCapacidad(estadioDatos.getCapacidad());
        existente.setDireccion(estadioDatos.getDireccion());

        return estadioRepository.save(existente);
    }
}