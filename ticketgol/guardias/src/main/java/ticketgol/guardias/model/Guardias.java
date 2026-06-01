
package ticketgol.guardias.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guardias")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Guardias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   //buscar estadio
    @NotNull(message = "El ID del estadio asignado es obligatorio")
    @Column(name = "estadio_id", nullable = false)
    private Long estadioId;

    @NotBlank(message = "El nombre del guardia es obligatorio")
    @Size(max = 100, message = "El nombre del guardia no puede exceder los 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El número de placa del guardia es obligatorio")
    @Size(max = 50, message = "El número de placa no puede exceder los 50 caracteres")
    @Column(name = "numero_placa", nullable = false, length = 50)
    private String numeroPlaca;


}
