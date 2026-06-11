package ticketgol.pases_temporada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioEstadoDto {
    private Long id;
    private String rut;
    private String estadoSancion;
<<<<<<< HEAD:ticketgol/pases_temporada_M/src/main/java/ticketgol/pases_temporada/model/UsuarioEstadoDto.java
}
=======
}
>>>>>>> main:ticketgol/pases_temporada/src/main/java/ticketgol/pases_temporada/model/UsuarioEstadoDto.java
