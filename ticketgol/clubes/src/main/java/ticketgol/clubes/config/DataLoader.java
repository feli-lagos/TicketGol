package ticketgol.clubes.config;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ticketgol.clubes.model.Club;
import ticketgol.clubes.repository.ClubRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClubRepository clubRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Verificar si la tabla ya tiene datos para no duplicar cada vez que reinicias
        if (clubRepository.count() == 0) {

            // Generar 15 clubes falsos
            for (int i = 0; i < 15; i++) {
                Club club = new Club();

                // Usamos DataFaker para simular datos reales de equipos
                club.setNombre(faker.team().name());
                club.setDivision(faker.options().option("Primera División", "Segunda División", "Tercera División"));
                club.setCorreo(faker.internet().emailAddress());

                clubRepository.save(club);
            }
            System.out.println("¡DataFaker: 15 clubes generados exitosamente!");
        }
    }
}
