package ticketgol.guardias.Dto;

import lombok.Data;

@Data
public class GuardiaResponseDTO {
    private Long id;
    private Long estadioId;
    private String nombre;
    private String numeroPlaca;
}