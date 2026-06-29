package ticketgol.tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.tickets.model.Ticket;
import ticketgol.tickets.service.TicketService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/ticket")
@Tag(name = "Tickets", description = "Operaciones relacionadas a la gestión de tickets/entradas")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los tickets", description = "Obtiene una lista de todos los tickets emitidos en la plataforma")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "204", description = "No hay tickets registrados")
    })
    public ResponseEntity<List<Ticket>> listTickets() {
        log.info("Recibida petición GET para listar todos los tickets");
        List<Ticket> listTickets = ticketService.findAllTickets();
        if (listTickets.isEmpty()) {
            log.warn("No se encontraron tickets en la base de datos, retornando 204 No Content");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listTickets);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un ticket por ID", description = "Obtiene los detalles de un ticket específico a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        log.info("Recibida petición GET para buscar el ticket con ID: {}", id);
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    @Operation(summary = "Registrar/Emitir un nuevo ticket", description = "Crea y guarda un nuevo ticket en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada del ticket inválidos")
    })
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        log.info("Recibida petición POST para registrar un nuevo ticket");
        Ticket ticketNuevo = ticketService.saveTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketNuevo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un ticket existente", description = "Modifica los datos de un ticket a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticketDetails) {
        log.info("Recibida petición PUT para actualizar el ticket con ID: {}", id);
        Ticket ticketActualizado = ticketService.updateTicket(id, ticketDetails);
        return ResponseEntity.ok(ticketActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un ticket", description = "Elimina un ticket del sistema de forma permanente a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        log.info("Recibida petición DELETE para eliminar el ticket con ID: {}", id);
        ticketService.deleteTicket(id);
        return ResponseEntity.ok("Ticket eliminado correctamente");
    }
}