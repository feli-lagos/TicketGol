package ticketgol.eventos.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ticketgol.eventos.dto.EventoDTO;
import ticketgol.eventos.model.Evento;
import ticketgol.eventos.repository.EventoRepository;

@Slf4j
@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final RestTemplate restTemplate;

    public EventoService(EventoRepository eventoRepository, RestTemplate restTemplate) {
        this.eventoRepository = eventoRepository;
        this.restTemplate = restTemplate;
    }

    public EventoDTO guardarEvento(EventoDTO eventoDto) {
        log.info("Iniciando registro de evento. Club Local ID: {}, Club Visitante ID: {}",
                eventoDto.getClubLocalId(), eventoDto.getClubVisitanteId());

        // Validación de existencia de ambos clubes en el microservicio externo
        validarClubExistente(eventoDto.getClubLocalId());
        validarClubExistente(eventoDto.getClubVisitanteId());

        Evento evento = convertirAEntidad(eventoDto);
        Evento eventoGuardado = eventoRepository.save(evento);

        log.info("Evento guardado exitosamente en la base de datos con ID: {}", eventoGuardado.getId());
        return convertirADto(eventoGuardado);
    }

    public List<EventoDTO> obtenerTodosLosEventos() {
        log.info("Consultando todos los eventos en la base de datos");
        List<Evento> eventos = eventoRepository.findAll();
        return eventos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private void validarClubExistente(Long clubId) {
        String urlClubes = "http://localhost:8110/api/clubes/" + clubId;
        try {
            log.info("Verificando existencia del club ID: {} mediante llamada síncrona al puerto 8110", clubId);
            restTemplate.getForEntity(urlClubes, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Validación fallida: El microservicio de Clubes confirmó que el ID {} no existe.", clubId);
            throw new RuntimeException("El club con ID " + clubId + " no existe en el sistema.");
        } catch (ResourceAccessException ex) {
            log.error("Error de red: El microservicio de Clubes (puerto 8110) se encuentra fuera de línea o inaccesible.");
            throw new RuntimeException("Error de conexión: El microservicio de Clubes (Puerto 8110) no está respondiendo.");
        }
    }

    // --- Métodos de Mapeo ---

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
        evento.setId(dto.getId());
        evento.setClubLocalId(dto.getClubLocalId());
        evento.setClubVisitanteId(dto.getClubVisitanteId());
        evento.setEstadioId(dto.getEstadioId());
        evento.setFechaEvento(dto.getFechaEvento());
        evento.setEstado(dto.getEstado());
        return evento;
    }
}