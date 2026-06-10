package ticketgol.pases_temporada.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.PaseTemporadaDtoFront;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T20:52:35-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 26 (Oracle Corporation)"
)
@Component
public class PaseMapperImpl implements PaseMapper {

    @Override
    public void updatePassFromDto(PaseTemporadaDtoFront dto, PaseTemporada entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getTemporada() != null ) {
            entity.setTemporada( dto.getTemporada() );
        }
        if ( dto.getFecha_final() != null ) {
            entity.setFecha_final( dto.getFecha_final() );
        }
        entity.setEstado( dto.isEstado() );
        if ( dto.getClubId() != null ) {
            entity.setClubId( dto.getClubId() );
        }
        if ( dto.getSeccionId() != null ) {
            entity.setSeccionId( dto.getSeccionId() );
        }
    }
}
