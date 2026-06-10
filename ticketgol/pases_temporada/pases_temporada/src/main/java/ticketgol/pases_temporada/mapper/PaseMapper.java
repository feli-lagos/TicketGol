package ticketgol.pases_temporada.mapper;

import org.springframework.stereotype.Component;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.PaseTemporadaDtoFront;

@Component
public class PaseMapper {

    public void updatePassFromDto(PaseTemporadaDtoFront dto, PaseTemporada entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getTemporada() != null) {
            entity.setTemporada(dto.getTemporada());
        }

        if (dto.getFecha_final() != null) {
            entity.setFecha_final(dto.getFecha_final());
        }

        if (dto.getClubId() != null) {
            entity.setClubId(dto.getClubId());
        }

        if (dto.getSeccionId() != null) {
            entity.setSeccionId(dto.getSeccionId());
        }

        entity.setEstado(dto.isEstado());
    }
}