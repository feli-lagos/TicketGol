package ticketgol.compras.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Date;

@Data
public class CompraRequestDTO {

    @NotBlank(message = "El ID del usuario es obligatorio no puede estar vacío")
    @Size(max = 100, message = "El ID no puede tener más de 100 caracteres")
    private String usuarioId;

    @NotBlank(message = "El ID del ticket es obligatorio no puede estar vacío")
    @Size(max = 100, message = "El ID de ticket no puede tener más de 100 caracteres")
    private String ticketId;

    @NotBlank(message = "El código QR es obligatorio")
    @Size(max = 100, message = "El código QR no puede tener más de 100 caracteres")
    private String codigoQr;

    @NotNull(message = "La fecha de compra es obligatoria")
    private Date fechaCompra;

    @NotBlank(message = "El método de pago es obligatorio.")
    @Size(max = 100, message = "El método de pago no puede tener más de 100 caracteres.")
    private String metodoPago;

    @NotBlank(message = "El estado de la compra es obligatorio.")
    @Size(max = 100, message = "El estado no puede tener más de 100 caracteres.")
    private String estado;
}