package ticketgol.tickets.testService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ticketgol.tickets.model.Ticket;
import ticketgol.tickets.repository.TicketRepository;
import ticketgol.tickets.service.TicketService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketTestService {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("Debe retornar una lista de tickets cuando existen registros en la BD")
    void shouldReturnTicketListWhenTicketsExist() {

        Ticket ticket1 = new Ticket(1L, 45, 15000, true, 10L, 2L, 5L);
        Ticket ticket2 = new Ticket(2L, 46, 15000, true, 11L, 2L, 5L);

        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Ticket> resultado = ticketService.findAllTickets();

        assertNotNull(resultado, "La lista de tickets no debería ser nula");
        assertEquals(2, resultado.size(), "Deberían retornar exactamente 2 tickets");
        assertEquals(45, resultado.get(0).getSeatNumber(), "El número de asiento del primer ticket debería ser 45");
        assertEquals(15000, resultado.get(0).getPrecio(), "El precio debería ser 15000");
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe lanzar una excepción cuando se busca un ticket por un ID que no existe")
    void shouldThrowExceptionWhenTicketNotFound() {

        Long idInexistente = 999L;
        when(ticketRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            ticketService.getTicketById(idInexistente);
        });
        verify(ticketRepository, times(1)).findById(idInexistente);
    }
}