package ticketgol.eventos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "club_local_id", nullable = false)
    private Long clubLocalId;

    @Column(name = "club_visitante_id", nullable = false)
    private Long clubVisitanteId;

    @Column(name = "estadio_id", nullable = false)
    private Long estadioId;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Column(nullable = false)
    private String estado;
}