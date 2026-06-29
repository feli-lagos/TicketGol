package ticketgol.compras.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El ID del usuario es obligatorio no puede estar vacío")
    @Size(max = 100, message = "El ID no puede tener más de 100 caracteres")
    @Column(name = "usuario_id", nullable = false, length = 100)
    private String usuarioId;

    @NotBlank(message = "El ID del ticket es obligatorio no puede estar vacío")
    @Size(max = 100, message = "El ID de ticket no puede tener más de 100 caracteres")
    @Column(name = "ticket_id", nullable = false, length = 100)
    private String ticketId;

    @NotBlank(message = "El código QR es obligatorio")
    @Size(max = 100, message = "El código QR no puede tener más de 100 caracteres")
    @Column(name = "codigo_qr", nullable = false, length = 100)
    private String codigoQr;

    @NotNull(message = "La fecha de compra es obligatoria")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_compra", nullable = false)
    private Date fechaCompra;

    @NotBlank(message = "El método de pago es obligatorio.")
    @Size(max = 100, message = "El método de pago no puede tener más de 100 caracteres.")
    @Column(name = "metodo_pago", nullable = false, length = 100)
    private String metodoPago;

    @NotBlank(message = "El estado de la compra es obligatorio.")
    @Size(max = 100, message = "El estado no puede tener más de 100 caracteres.")
    @Column(name = "estado", nullable = false, length = 100)
    private String estado;
}