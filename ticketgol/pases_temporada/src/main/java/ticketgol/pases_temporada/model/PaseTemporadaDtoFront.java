package ticketgol.pases_temporada.model;

<<<<<<< HEAD:ticketgol/pases_temporada_M/src/main/java/ticketgol/pases_temporada/model/PaseTemporadaDtoFront.java

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
=======
import java.sql.Date;

>>>>>>> main:ticketgol/pases_temporada/src/main/java/ticketgol/pases_temporada/model/PaseTemporadaDtoFront.java
public class PaseTemporadaDtoFront {
    private Long id;
    private String temporada;
    private Date fecha_final;
<<<<<<< HEAD:ticketgol/pases_temporada_M/src/main/java/ticketgol/pases_temporada/model/PaseTemporadaDtoFront.java
    private boolean estado;
    private Long clubId;
    private Long seccionId;
}
=======
    private boolean estado; // <-- Lee la nota importante abajo sobre este boolean
    private Long clubId;
    private Long seccionId;

    // Constructor vacío
    public PaseTemporadaDtoFront() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public Date getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(Date fecha_final) {
        this.fecha_final = fecha_final;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public Long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(Long seccionId) {
        this.seccionId = seccionId;
    }
}
>>>>>>> main:ticketgol/pases_temporada/src/main/java/ticketgol/pases_temporada/model/PaseTemporadaDtoFront.java
