package ticketgol.pases_temporada.mapper;

import org.springframework.stereotype.Component;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;
import java.util.Map;

@Component
public class UsuarioMapper {

    public UsuarioEstadoDto toDto(Map<String, Object> externalUser) {
        if (externalUser == null) {
            return null;
        }

        UsuarioEstadoDto dto = new UsuarioEstadoDto();

        if (externalUser.get("id") != null) {
            dto.setId(((Number) externalUser.get("id")).longValue());
        }

        if (externalUser.get("rut") != null) {
            dto.setRut((String) externalUser.get("rut"));
        }

        if (externalUser.get("estadoSancion") != null) {
            dto.setEstadoSancion((String) externalUser.get("estadoSancion"));
        }

        return dto;
    }
}