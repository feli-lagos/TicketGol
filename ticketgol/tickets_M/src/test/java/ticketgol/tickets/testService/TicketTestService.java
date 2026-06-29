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
    @DisplayName("Debe retornar lista de tickets")
    void shouldReturnTicketListWhenTicketsExist() {


        Ticket ticket1 =
                new Ticket(1L,45,15000,true,10L,2L,5L);

        Ticket ticket2 =
                new Ticket(2L,46,15000,true,11L,2L,5L);



        when(ticketRepository.findAll())
                .thenReturn(Arrays.asList(ticket1,ticket2));



        List<Ticket> resultado =
                ticketService.findAllTickets();



        System.out.println("TEST obtener tickets:");
        System.out.println("Tickets encontrados: " + resultado.size());

        for(Ticket t : resultado){
            System.out.println(
                    "Ticket ID: " + t.getId() +
                            " Asiento: " + t.getSeatNumber() +
                            " Precio: " + t.getPrecio()
            );
        }



        assertNotNull(resultado);
        assertEquals(2,resultado.size());


        verify(ticketRepository)
                .findAll();
    }




    @Test
    @DisplayName("Debe mostrar mensaje cuando ticket no existe")
    void shouldThrowExceptionWhenTicketNotFound() {


        Long idInexistente = 999L;


        when(ticketRepository.findById(idInexistente))
                .thenReturn(Optional.empty());



        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> ticketService.getTicketById(idInexistente)
                );



        System.out.println("ERROR CONTROLADO:");
        System.out.println(exception.getMessage());



        verify(ticketRepository)
                .findById(idInexistente);
    }
}