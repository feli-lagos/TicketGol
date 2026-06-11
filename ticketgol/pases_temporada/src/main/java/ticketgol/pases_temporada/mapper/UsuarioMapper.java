package ticketgol.pases_temporada.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.PaseTemporadaDtoFront;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;

import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface UsuarioMapper {
    @Mapping(target = "id", expression = "java(((Number) externalUser.get(\"id\")).longValue())")
    @Mapping(target = "rut", expression = "java((String) externalUser.get(\"rut\"))")
<<<<<<< HEAD:ticketgol/pases_temporada_M/src/main/java/ticketgol/pases_temporada/mapper/UsuarioMapper.java
    @Mapping(target = "estadoSancion", expression = "java((String) externalUser.get(\"estadoSancion\"))")
=======
    @Mapping(target = "estadoSancion", expression = "java(externalUser.get(\"estadoSancion\") != null ? (String) externalUser.get(\"estadoSancion\") : \"SIN_SANCION\")")
>>>>>>> main:ticketgol/pases_temporada/src/main/java/ticketgol/pases_temporada/mapper/UsuarioMapper.java

    UsuarioEstadoDto toDto(Map<String,Object> externalUser);
}
