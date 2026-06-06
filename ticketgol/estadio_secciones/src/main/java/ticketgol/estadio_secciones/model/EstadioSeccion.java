package ticketgol.estadio_secciones.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadio_secciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadioSeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del estadio es obligatorio")
    @Column(name = "estadio_id", nullable = false)
    private Long estadioId;

    @NotBlank(message = "El nombre de la sección es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "La cantidad de asientos es obligatoria")
    @Min(value = 1, message = "La cantidad de asientos debe ser mayor a 0")
    @Column(name = "cantidad_asientos", nullable = false)
    private Integer cantidadAsientos;
}