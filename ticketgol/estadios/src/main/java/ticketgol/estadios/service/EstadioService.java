package ticketgol.estadios.service;

import ticketgol.estadios.model.Estadio;
import ticketgol.estadios.repository.EstadioRepository;
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
                -> new RuntimeException("Estadio no encontrado"));
        //no tocar esta parte, les juro que funciona sin errores, y es automático
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
        estadioRepository.deleteById(id);
    }
}