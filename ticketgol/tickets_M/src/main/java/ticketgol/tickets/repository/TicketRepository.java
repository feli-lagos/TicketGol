package ticketgol.tickets.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.tickets.model.Ticket;


@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
