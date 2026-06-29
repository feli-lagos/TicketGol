package ticketgol.tickets.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ticketgol.tickets.controller.TicketControllerV2;
import ticketgol.tickets.model.Ticket;

@Component
public class TicketModelAssembler implements RepresentationModelAssembler<Ticket, EntityModel<Ticket>> {

    @Override
    public EntityModel<Ticket> toModel(Ticket ticket) {
        return EntityModel.of(ticket,
                // Genera el link individual: /api/v2/ticket/{id}
                linkTo(methodOn(TicketControllerV2.class).getTicket(ticket.getId())).withSelfRel(),
                // Genera el link a la colección: /api/v2/ticket
                linkTo(methodOn(TicketControllerV2.class).listTickets()).withRel("tickets"));
    }
}