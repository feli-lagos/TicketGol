package ticketgol.tickets.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket")
public class ticket {
    private Long id;
    private int seatNumber;
    private double precio;
    private boolean status;
    private Long sectionId;
    private Long eventId;
}
