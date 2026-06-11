package ticketgol.pases_temporada.mapper;

import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-10T01:54:59-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 26 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioEstadoDto toDto(Map<String, Object> externalUser) {
        if ( externalUser == null ) {
            return null;
        }

        UsuarioEstadoDto usuarioEstadoDto = new UsuarioEstadoDto();

        usuarioEstadoDto.setId( ((Number) externalUser.get("id")).longValue() );
        usuarioEstadoDto.setRut( (String) externalUser.get("rut") );
        usuarioEstadoDto.setEstadoSancion( (String) externalUser.get("estadoSancion") );

        return usuarioEstadoDto;
    }
}
