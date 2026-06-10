package ticketgol.pases_temporada.model; // Asegúrate de poner el paquete correcto

import lombok.Data;

@Data
public class UsuarioResponse {
    private Long id;
    private String rut;
    private String estadoSancion;
    // ... agrega cualquier otro campo que venga en el JSON del otro servicio
}