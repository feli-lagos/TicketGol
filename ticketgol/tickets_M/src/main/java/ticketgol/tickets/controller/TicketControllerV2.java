package ticketgol.tickets.controller;

import ticketgol.tickets.assemblers.TicketModelAssembler;
import ticketgol.tickets.model.Ticket;
import ticketgol.tickets.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/ticket")
public class TicketControllerV2 {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Ticket>> listTickets() {
        // Obtenemos la lista de entidades Ticket y las convertimos a modelos con links
        List<EntityModel<Ticket>> tickets = ticketService.findAllTickets().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(tickets,
                linkTo(methodOn(TicketControllerV2.class).listTickets()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Ticket> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return assembler.toModel(ticket);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Ticket>> createTicket(@Valid @RequestBody Ticket ticket) {
        Ticket ticketNuevo = ticketService.saveTicket(ticket);

        return ResponseEntity
                .created(linkTo(methodOn(TicketControllerV2.class).getTicket(ticketNuevo.getId())).toUri())
                .body(assembler.toModel(ticketNuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Ticket>> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticketDetails) {
        Ticket ticketActualizado = ticketService.updateTicket(id, ticketDetails);

        return ResponseEntity
                .ok(assembler.toModel(ticketActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);

        return ResponseEntity.noContent().build();
    }
}