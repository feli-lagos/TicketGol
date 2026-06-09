package ticketgol.pases_temporada.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pases_temporada")
@Entity
public class PasesTemporada {

    private Long id;
    private String temporada;
    private Date fecha_inicio;
    private Date fecha_final;
    private boolean estado;
    private Long userId;
    private Long clubId;
    private Long seccionId;

}
