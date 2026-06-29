package ticketgol.tickets.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketgol.tickets.model.Ticket;
import ticketgol.tickets.repository.TicketRepository;
import ticketgol.tickets.webClient.TicketUsuario;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketUsuario ticketUsuario;

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
        }

    public Ticket saveTicket(Ticket ticket) {
        Map<String, Object> usuario = ticketUsuario.getUsuario(ticket.getUserId());
        if (usuario == null || usuario.isEmpty()) {
            throw new RuntimeException("El usuario no existe");
        }
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
    }

    // Actualizar
    public Ticket updateTicket(Long id, Ticket ticketNuevo) {
        Ticket ticket = getTicketById(id);
        ticket.setPrecio(ticketNuevo.getPrecio());
        ticket.setSeatNumber(ticketNuevo.getSeatNumber());
        return ticketRepository.save(ticket);
    }

    // Eliminar
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, ticket no encontrado");
        }
        ticketRepository.deleteById(id);
    }
}
