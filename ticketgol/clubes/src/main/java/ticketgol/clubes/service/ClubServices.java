package ticketgol.clubes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ticketgol.clubes.dto.ClubDTO;
import ticketgol.clubes.exception.ClubNotFoundException;
import ticketgol.clubes.model.Club;
import ticketgol.clubes.repository.ClubRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClubServices {

    private final ClubRepository clubRepository;

    public ClubServices(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<ClubDTO> obtenerTodos() {
        log.info("Consultando todos los clubes en la base de datos");
        return clubRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public ClubDTO buscarPorId(Long id) {
        log.info("Buscando club con ID: {}", id);
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error en búsqueda: No se encontró el club con ID {}", id);
                    return new ClubNotFoundException("No se encontró el club con el ID: " + id);
                });
        return convertirADto(club);
    }

    public ClubDTO guardarClub(ClubDTO clubDto) {
        log.info("Iniciando registro de nuevo club: {}", clubDto.getNombre());
        Club club = convertirAEntidad(clubDto);
        Club clubGuardado = clubRepository.save(club);

        log.info("Club guardado exitosamente en la base de datos con ID: {}", clubGuardado.getId());
        return convertirADto(clubGuardado);
    }

    // --- MÉTODOS DE MAPEO ---

    private ClubDTO convertirADto(Club club) {
        return new ClubDTO(club.getId(), club.getNombre(), club.getDivision(), club.getCorreo());
    }

    private Club convertirAEntidad(ClubDTO dto) {
        Club club = new Club();
        club.setId(dto.getId());
        club.setNombre(dto.getNombre());
        club.setDivision(dto.getDivision());
        club.setCorreo(dto.getCorreo());
        return club;
    }
}