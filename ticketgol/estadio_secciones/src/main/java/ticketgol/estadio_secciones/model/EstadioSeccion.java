package ticketgol.estadio_secciones.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) //así no sale en el Swagger
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
    @Max(value = 200000, message = "La cantidad de asientos no puede ser una locura (máximo 200.000)")
    @Column(name = "cantidad_asientos", nullable = false)
    private Integer cantidadAsientos;
}