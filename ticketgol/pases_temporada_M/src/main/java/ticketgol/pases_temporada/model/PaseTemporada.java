package ticketgol.pases_temporada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pases_temporada")
@Entity
public class PaseTemporada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "temporada", length = 50, nullable = false)
    @Size(min = 5, max = 50)
    @NotBlank(message = "la temporada no puede estar en blanco")
    private String temporada;

    @JsonProperty("fecha_inicio")
    @Column(name = "fecha_inicio", nullable = false)
    @NotNull(message = "la fecha no puede estar vacía")
    private LocalDate fecha_inicio;

    @JsonProperty("fecha_final")
    @Column(name = "fecha_final", nullable = false)
    @NotNull(message = "el pase de temporada debe tener fecha de término")
    private LocalDate fecha_final;

    @Column(name = "estado", nullable = false)
    @NotNull(message = "el estado debe estar vigente o no")
    private boolean estado;

    @JsonProperty("userRut")
    @Column(name = "user_rut", nullable = false)
    @NotBlank(message = "el RUT del usuario no puede estar en blanco")
    private String userRut;

    @JsonProperty("clubId")
    @Column(name = "club_id", nullable = false)
    @NotNull(message = "el id no puede estar en blanco")
    private Long clubId;

    @JsonProperty("seccionId")
    @Column(name = "seccion_id", nullable = false)
    @NotNull(message = "el id no puede estar en blanco")
    private Long seccionId;
}