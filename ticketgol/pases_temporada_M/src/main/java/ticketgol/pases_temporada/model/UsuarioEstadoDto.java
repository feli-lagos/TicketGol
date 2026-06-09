package ticketgol.pases_temporada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UsuarioEstadoDto {
    private Long id;
    private String rut;
    private String correo;
    private String nombreVisible;
    private String telefono;
    private String hashClave;
    private LocalDateTime fechaCreacion;
    private String estadoSancion;
}
