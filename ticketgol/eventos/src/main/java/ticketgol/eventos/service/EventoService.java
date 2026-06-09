package ticketgol.eventos.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ticketgol.eventos.dto.EventoDTO;
import ticketgol.eventos.model.Evento;
import ticketgol.eventos.repository.EventoRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final RestTemplate restTemplate;

    public EventoService(EventoRepository eventoRepository, RestTemplate restTemplate) {
        this.eventoRepository = eventoRepository;
        this.restTemplate = restTemplate;
    }

    public EventoDTO guardarEvento(EventoDTO eventoDto) {
        // Validación de existencia de ambos clubes en el microservicio externo
        validarClubExistente(eventoDto.getClubLocalId());
        validarClubExistente(eventoDto.getClubVisitanteId());

        Evento evento = convertirAEntidad(eventoDto);
        Evento eventoGuardado = eventoRepository.save(evento);
        return convertirADto(eventoGuardado);
    }

    private void validarClubExistente(Long clubId) {
        String urlClubes = "http://localhost:8110/api/clubes/" + clubId;
        try {
            restTemplate.getForEntity(urlClubes, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new RuntimeException("El club con ID " + clubId + " no existe en el sistema.");
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