package ticketgol.guardias.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GuardiaRequestDTO {

    @NotNull(message = "El ID del estadio asignado es obligatorio")
    private Long estadioId;

    @NotBlank(message = "El nombre del guardia es obligatorio")
    @Size(max = 100, message = "El nombre del guardia no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El número de placa del guardia es obligatorio")
    @Size(max = 50, message = "El número de placa no puede exceder los 50 caracteres")
    private String numeroPlaca;
}