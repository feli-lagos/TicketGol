package ticketgol.registro_accesos.Modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name ="registro_acceso")
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id de la compra no puede ser nulo")
    @Column(name = "comprasid", nullable = false)
    private Long comprasid;

    @NotNull(message = "El estado de acceso (permitido/denegado) no puede ser nulo")
    @Column(name = "accessgranted", nullable = false)
    private Boolean accessgranted;

    @NotNull(message = "El id de guardia no puede ser nulo")
    @Column(name = "guardiaid", nullable = false)
    private Long guardiaid;

    @Column(name = "scantime")
    private LocalDateTime scantime;
}