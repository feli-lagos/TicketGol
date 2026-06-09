package ticketgol.pases_temporada.mapper;


import org.mapstruct.Mapper;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioEstadoDto toDto(Map<String,Object> externalUser);
}
