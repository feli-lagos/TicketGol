package ticketgol.pases_temporada.model;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
public class PaseTemporadaDtoFront {
    private Long id;
    private String temporada;
    private Date fecha_final;
    private boolean estado;
    private Long clubId;
    private Long seccionId;
}
