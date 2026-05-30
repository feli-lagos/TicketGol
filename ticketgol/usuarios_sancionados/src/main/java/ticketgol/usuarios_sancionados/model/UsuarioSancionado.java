package ticketgol.usuarios_sancionados.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "usuarios_sancionados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSancionado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 7, max = 13, message = "El RUN debe tener entre 7 y 13 caracteres")
    @Column(unique = true, length = 13, nullable = false)
    private String rut;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 255, message = "El motivo no puede exceder 255 caracteres")
    @Column(nullable = false)
    private String motivo;

    @PastOrPresent(message = "La fecha de sanción no puede ser futura")
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaSancion;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fechaExpiracion;
}