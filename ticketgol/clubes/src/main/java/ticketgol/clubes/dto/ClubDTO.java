package ticketgol.clubes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {

    private Long id;

    @NotBlank(message = "El nombre del club es obligatorio")
    private String nombre;

    @NotBlank(message = "La división es obligatoria")
    private String division;

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "Debe proporcionar un correo válido")
    private String correo;
}