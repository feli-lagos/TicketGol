package ticketgol.tickets.config;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ticketgol.tickets.model.Ticket;
import ticketgol.tickets.repository.TicketRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Evitamos duplicar datos si la tabla de tickets ya tiene registros
        if (ticketRepository.count() == 0) {

            // Generamos 20 tickets de prueba
            for (int i = 0; i < 20; i++) {
                Ticket ticket = new Ticket();

                ticket.setSeatNumber(faker.number().numberBetween(1, 150)); // Asiento del 1 al 150
                ticket.setPrecio(faker.options().option(15000, 25000, 45000, 60000)); // Precios fijos de entradas
                ticket.setStatus(faker.bool().bool());
                ticket.setUserId(faker.number().numberBetween(1L, 100L));    // IDs de usuario del 1 al 100
                ticket.setEstadioId(faker.number().numberBetween(1L, 10L));   // IDs de estadios del 1 al 10
                ticket.setEventId(faker.number().numberBetween(1L, 5L));      // IDs de eventos del 1 al 5

                ticketRepository.save(ticket);
            }
            System.out.println("¡DataFaker: 20 Tickets generados exitosamente en el perfil DEV!");
        }
    }
}