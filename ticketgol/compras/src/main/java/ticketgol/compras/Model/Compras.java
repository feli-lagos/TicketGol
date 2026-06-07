package ticketgol.compras.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @NotBlank (message = "El ID del usuario es obligatorio no puede estar vacio")
    @Size (max = 100 ,message = "EL ID no puede tener mas de 100 caracteres")
    @Column(name = "userId",nullable = false,length = 100)
    private  String usuarioId;

    @NotBlank (message = "El ID del ticket es obligatorio no puede estar vacio")
    @Size (max = 100,message = "El ID de ticket no puede tener mas de 100 caracteres")
    @Column (name = "ticketId",nullable = false,length = 100)
    private String ticketId;

    @NotBlank(message = "El codigo QR es obligatorio ")
    @Size(max = 100,message = "El codigo QR no puede tener mas de 100 caracteres ")
    @Column(name = "qrCode")
    private String codigoQr;

    @NotBlank(message = "El método de pago es obligatorio.")
    @Size(max = 100, message = "El método de pago no puede tener más de 100 caracteres.")
    @Column(name = "metodoPago", nullable = false, length = 100)
    private String metodoPago;

    @NotNull(message = "El estado de la compra es obligatorio.")
    @Size(max = 100, message = "El estado no puede tener más de 100 caracteres.")
    @Column(name = "status", nullable = false, length = 100)
    private Boolean estado;



}
