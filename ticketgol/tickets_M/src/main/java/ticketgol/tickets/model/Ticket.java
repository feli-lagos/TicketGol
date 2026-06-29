package ticketgol.tickets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Min(value = 1, message = "El número de asiento debe ser igual o mayor a 1")
    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @NotNull
    @Column(name = "precio", nullable = false)
    private int precio;

    @Column(name = "status", nullable = false)
    private boolean status;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "section_id", nullable = false)
    private Long estadioId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @PrePersist
    protected void onCreate() {
        this.status = true;
    }
}
