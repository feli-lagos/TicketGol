package ticketgol.usuarios_sancionados.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios_sancionados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSancionado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) //así no sale en el Swagger
    private Long id;

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 7, max = 13, message = "El RUT debe tener entre 7 y 13 caracteres")
    @Column(unique = true, length = 13, nullable = false)
    private String rut;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 255, message = "El motivo no puede exceder 255 caracteres")
    @Column(nullable = false)
    private String motivo;

    @NotNull(message = "La fecha de sanción es obligatoria")
    @PastOrPresent(message = "La fecha de sanción no puede ser futura")
    @Column(nullable = false)
    private LocalDate fechaSancion;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaExpiracion;
}