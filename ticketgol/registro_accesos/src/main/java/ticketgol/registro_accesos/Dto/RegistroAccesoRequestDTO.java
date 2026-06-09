package ticketgol.registro_accesos.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroAccesoRequestDTO {

    @NotNull(message = "El ID de la compra no puede ser nulo")
    private Long comprasid;

    @NotNull(message = "El estado de acceso (permitido/denegado) no puede ser nulo")
    private Boolean accessgranted;

    @NotNull(message = "El ID del guardia no puede ser nulo")
    private Long guardiaid;
}