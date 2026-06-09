package ticketgol.registro_accesos.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegistroAccesoResponseDTO {
    private Long id;
    private Long comprasid;
    private Boolean accessgranted;
    private Long guardiaid;
    private LocalDateTime scantime;
}