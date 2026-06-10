package ticketgol.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketgol.eventos.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}