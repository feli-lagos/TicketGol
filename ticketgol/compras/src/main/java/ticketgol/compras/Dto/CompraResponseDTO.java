package ticketgol.compras.Dto;

import lombok.Data;
import java.util.Date;

@Data
public class CompraResponseDTO {
    private Long id;
    private String usuarioId;
    private String ticketId;
    private String codigoQr;
    private Date fechaCompra;
    private String metodoPago;
    private String estado;
}