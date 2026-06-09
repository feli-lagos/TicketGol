package ticketgol.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSancionadoDTO {
    private Long id;
    private String rut;
    private String motivo;
    private LocalDate fechaSancion;
    private LocalDate fechaExpiracion;
}