package ticketgol.pases_temporada.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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

    @Column(name = "fecha_inicio", length = 50, nullable = false)
    @NotNull(message = "la fecha no puede estar vacía")
    private Date fecha_inicio;

    @Column(name = "fecha_final", length = 50, nullable = false)
    @NotNull(message = "el pase de temporada debe tener fecha de término")
    private Date fecha_final;

    @Column(name = "estado", length = 50, nullable = false)
    @NotNull(message = "el estado debe estar vigente o no")
    private boolean estado;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "el id no puede estar en blanco")
    private Long userId;

    @Column(name = "club_id", nullable = false)
    @NotNull(message = "el id no puede estar en blanco")
    private Long clubId;

    @Column(name = "seccion_id", nullable = false)
    @NotNull(message = "el id no puede estar en blanco")
    private Long seccionId;

}