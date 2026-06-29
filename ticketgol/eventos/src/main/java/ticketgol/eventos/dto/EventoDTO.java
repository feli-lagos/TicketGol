package ticketgol.eventos.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO {

    private Long id;

    @NotNull(message = "El ID del club local es obligatorio")
    private Long clubLocalId;

    @NotNull(message = "El ID del club visitante es obligatorio")
    private Long clubVisitanteId;

    @NotNull(message = "El ID del estadio es obligatorio")
    private Long estadioId;

    @NotNull(message = "La fecha del evento es obligatoria")
    @Future(message = "La fecha del evento debe ser en el futuro")
    private LocalDateTime fechaEvento;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}