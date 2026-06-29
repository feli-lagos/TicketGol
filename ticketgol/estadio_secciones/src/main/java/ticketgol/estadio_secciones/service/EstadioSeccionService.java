package ticketgol.estadio_secciones.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ticketgol.estadio_secciones.dto.EstadioDTO;
import ticketgol.estadio_secciones.exception.EstadioNotFoundException;
import ticketgol.estadio_secciones.exception.EstadioSeccionNotFoundException;
import ticketgol.estadio_secciones.model.EstadioSeccion;
import ticketgol.estadio_secciones.repository.EstadioSeccionRepository;

import java.util.List;

@Service
@Transactional
public class EstadioSeccionService {

    private static final Logger log = LoggerFactory.getLogger(EstadioSeccionService.class);

    @Autowired
    private EstadioSeccionRepository seccionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // URL fija (hardcoded) solicitada para la entrega
    private final String baseUrl = "http://localhost:8090/api/v1/estadios";

    private void validarEstadioExterno(Long estadioId) {
        try {
            String url = baseUrl + "/" + estadioId;
            log.debug("Validando existencia del Estadio ID: {} en la URL fija: {}", estadioId, url);
            restTemplate.getForObject(url, EstadioDTO.class);
            log.debug("Validación exitosa: El Estadio ID {} existe en el sistema", estadioId);
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            log.warn("Validación denegada: El Estadio con ID {} no existe en el sistema remoto", estadioId);
            throw new EstadioNotFoundException("Validación fallida: El Estadio con ID " + estadioId + " no existe.");
        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest e) {
            log.error("Error en formato de ID enviado al microservicio externo: {}", estadioId);
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "El formato del ID enviado es inválido.");
        } catch (Exception e) {
            log.error("Error de comunicación de red con el servicio de Estadios. Causa: {}", e.getMessage());
            throw new RuntimeException("Error de comunicación con el microservicio de Estadios: " + e.getMessage());
        }
    }

    public List<EstadioSeccion> findAll() {
        log.debug("Recuperando todas las secciones de estadios");
        return seccionRepository.findAll();
    }

    public EstadioSeccion findById(Long id) {
        log.debug("Buscando sección por ID: {}", id);
        return seccionRepository.findById(id).orElseThrow(() -> {
            log.error("Búsqueda fallida: No existe ninguna sección con ID: {}", id);
            return new EstadioSeccionNotFoundException("Sección no encontrada con el ID: " + id);
        });
    }

    public List<EstadioSeccion> findByEstadioId(Long estadioId) {
        log.debug("Recuperando todas las secciones asociadas al Estadio ID: {}", estadioId);
        return seccionRepository.findByEstadioId(estadioId);
    }

    public EstadioSeccion save(EstadioSeccion seccion) {
        log.info("Solicitud para registrar la sección [{}] para el Estadio ID: {}", seccion.getNombre(), seccion.getEstadioId());
        validarEstadioExterno(seccion.getEstadioId());

        EstadioSeccion guardado = seccionRepository.save(seccion);
        log.info("Sección guardada con éxito. ID generado por base de datos: {}", guardado.getId());
        return guardado;
    }

    public EstadioSeccion update(Long id, EstadioSeccion seccion) {
        log.info("Solicitud para actualizar la sección ID: {}", id);
        EstadioSeccion existente = findById(id);
        validarEstadioExterno(seccion.getEstadioId());

        existente.setEstadioId(seccion.getEstadioId());
        existente.setNombre(seccion.getNombre());
        existente.setCantidadAsientos(seccion.getCantidadAsientos());

        EstadioSeccion actualizado = seccionRepository.save(existente);
        log.info("Sección ID {} modificada con éxito", id);
        return actualizado;
    }

    public void delete(Long id) {
        log.info("Solicitud para eliminar la sección ID: {}", id);
        if (!seccionRepository.existsById(id)) {
            log.error("Error al borrar: La sección ID {} no existe en la base de datos", id);
            throw new EstadioSeccionNotFoundException("No se puede eliminar. Sección no encontrada con el ID: " + id);
        }
        seccionRepository.deleteById(id);
        log.info("Sección ID {} eliminada permanentemente", id);
    }
}