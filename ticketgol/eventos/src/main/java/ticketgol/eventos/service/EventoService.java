package ticketgol.eventos.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ticketgol.eventos.dto.EventoDTO;
import ticketgol.eventos.exception.EventoNotFoundException;
import ticketgol.eventos.model.Evento;
import ticketgol.eventos.repository.EventoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final RestTemplate restTemplate;

    public EventoService(EventoRepository eventoRepository, RestTemplate restTemplate) {
        this.eventoRepository = eventoRepository;
        this.restTemplate = restTemplate;
    }

    public List<EventoDTO> obtenerTodosLosEventos() {
        log.info("Consultando todos los eventos en la base de datos");
        return eventoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public EventoDTO buscarPorId(Long id) {
        log.info("Buscando evento con ID: {}", id);
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error en búsqueda: No se encontró el evento con ID {}", id);
                    return new EventoNotFoundException("No se encontró el evento con el ID: " + id);
                });
        return convertirADto(evento);
    }

    public EventoDTO guardarEvento(EventoDTO eventoDto) {
        log.info("Iniciando registro de evento. Club Local ID: {}, Club Visitante ID: {}",
                eventoDto.getClubLocalId(), eventoDto.getClubVisitanteId());

        validarClubExistente(eventoDto.getClubLocalId());
        validarClubExistente(eventoDto.getClubVisitanteId());

        Evento evento = convertirAEntidad(eventoDto);
        Evento eventoGuardado = eventoRepository.save(evento);

        log.info("Evento guardado exitosamente en la base de datos con ID: {}", eventoGuardado.getId());
        return convertirADto(eventoGuardado);
    }

    public EventoDTO actualizarEvento(Long id, EventoDTO eventoDto) {
        log.info("Intentando actualizar evento con ID: {}", id);

        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar: No se encontró el evento con ID {}", id);
                    return new EventoNotFoundException("No se encontró el evento con el ID: " + id);
                });

        if (!eventoExistente.getClubLocalId().equals(eventoDto.getClubLocalId())) {
            log.info("El Club Local ID fue modificado. Iniciando validación...");
            validarClubExistente(eventoDto.getClubLocalId());
        }
        if (!eventoExistente.getClubVisitanteId().equals(eventoDto.getClubVisitanteId())) {
            log.info("El Club Visitante ID fue modificado. Iniciando validación...");
            validarClubExistente(eventoDto.getClubVisitanteId());
        }

        eventoExistente.setClubLocalId(eventoDto.getClubLocalId());
        eventoExistente.setClubVisitanteId(eventoDto.getClubVisitanteId());
        eventoExistente.setEstadioId(eventoDto.getEstadioId());
        eventoExistente.setFechaEvento(eventoDto.getFechaEvento());
        eventoExistente.setEstado(eventoDto.getEstado());

        Evento eventoGuardado = eventoRepository.save(eventoExistente);
        log.info("Evento con ID: {} actualizado correctamente", id);

        return convertirADto(eventoGuardado);
    }

    public void eliminarEvento(Long id) {
        log.info("Intentando eliminar evento con ID: {}", id);

        if (!eventoRepository.existsById(id)) {
            log.error("Error al eliminar: No se encontró el evento con ID {}", id);
            throw new EventoNotFoundException("No se encontró el evento con el ID: " + id);
        }

        eventoRepository.deleteById(id);
        log.info("Evento con ID: {} eliminado exitosamente de la base de datos", id);
    }

    // --- MÉTODOS PRIVADOS ---

    private void validarClubExistente(Long clubId) {
        String urlClubes = "http://localhost:8110/api/clubes/" + clubId;
        try {
            log.info("Verificando existencia del club ID: {} mediante llamada síncrona al puerto 8110", clubId);
            restTemplate.getForEntity(urlClubes, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Validación fallida: El microservicio de Clubes confirmó que el ID {} no existe.", clubId);
            throw new EventoNotFoundException("El club con ID " + clubId + " no existe en el sistema.");
        } catch (ResourceAccessException ex) {
            log.error("Error de red: El microservicio de Clubes (puerto 8110) se encuentra fuera de línea o inaccesible.");
            throw new RuntimeException("Error de conexión: El microservicio de Clubes (Puerto 8110) no está respondiendo.");
        }
    }

    private EventoDTO convertirADto(Evento evento) {
        return new EventoDTO(
                evento.getId(),
                evento.getClubLocalId(),
                evento.getClubVisitanteId(),
                evento.getEstadioId(),
                evento.getFechaEvento(),
                evento.getEstado()
        );
    }

    private Evento convertirAEntidad(EventoDTO dto) {
        Evento evento = new Evento();
        if (dto.getId() != null) {
            evento.setId(dto.getId());
        }
        evento.setClubLocalId(dto.getClubLocalId());
        evento.setClubVisitanteId(dto.getClubVisitanteId());
        evento.setEstadioId(dto.getEstadioId());
        evento.setFechaEvento(dto.getFechaEvento());
        evento.setEstado(dto.getEstado());
        return evento;
    }
}