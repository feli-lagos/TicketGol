package ticketgol.pases_temporada.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.PaseTemporadaDtoFront;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;

import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UsuarioMapper {
    UsuarioEstadoDto toDto(Map<String,Object> externalUser);
}
