package ticketgol.estadio_secciones.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ticketgol.estadio_secciones.dto.EstadioDTO;
import ticketgol.estadio_secciones.exception.EstadioNotFoundException;
import ticketgol.estadio_secciones.exception.EstadioSeccionNotFoundException;
import ticketgol.estadio_secciones.model.EstadioSeccion;
import ticketgol.estadio_secciones.repository.EstadioSeccionRepository;

import java.util.List;

@Service
@Transactional
public class EstadioSeccionService {

    @Autowired
    private EstadioSeccionRepository seccionRepository;

    // acá dejé hardcodeada no más las URL según lo q acordamos
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8090/api/v1/estadios")
            .build();

    private void validarEstadioExterno(Long estadioId) {
        try {
            webClient.get()
                    .uri("/" + estadioId)
                    .retrieve()
                    .bodyToMono(EstadioDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new EstadioNotFoundException("Validación fallida: El Estadio con ID " + estadioId + " no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error de comunicación con el microservicio de Estadios: " + e.getMessage());
        }
    }

    // method pa buscar todos los estadios
    public List<EstadioSeccion> findAll() {
        return seccionRepository.findAll();
    }

    // este busca por id
    public EstadioSeccion findById(Long id) {
        return seccionRepository.findById(id).orElseThrow(() ->
                new EstadioSeccionNotFoundException("Sección no encontrada con el ID: " + id));
    }

    // este busca por id de estadio
    public List<EstadioSeccion> findByEstadioId(Long estadioId) {
        return seccionRepository.findByEstadioId(estadioId);
    }

    // este method es para guardar
    public EstadioSeccion save(EstadioSeccion seccion) {
        validarEstadioExterno(seccion.getEstadioId());
        return seccionRepository.save(seccion);
    }

    // method de actualizar
    public EstadioSeccion update(Long id, EstadioSeccion seccion) {
        EstadioSeccion existente = findById(id);
        validarEstadioExterno(seccion.getEstadioId());

        existente.setEstadioId(seccion.getEstadioId());
        existente.setNombre(seccion.getNombre());
        existente.setCantidadAsientos(seccion.getCantidadAsientos());

        return seccionRepository.save(existente);
    }

    //method de borrado, y chequea el error de intentar borrar algo q no está
    public void delete(Long id) {
        if (!seccionRepository.existsById(id)) {
            throw new EstadioSeccionNotFoundException("No se puede eliminar. Sección no encontrada con el ID: " + id);
        }
        seccionRepository.deleteById(id);
    }
}