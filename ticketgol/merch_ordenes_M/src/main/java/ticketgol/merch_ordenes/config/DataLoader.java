package ticketgol.merch_ordenes.config;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ticketgol.merch_ordenes.model.MerchOrden;
import ticketgol.merch_ordenes.repository.MerchRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MerchRepository merchRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Evitamos duplicar datos si la tabla ya tiene registros
        if (merchRepository.count() == 0) {

            // Generamos 15 órdenes de merchandising de prueba
            for (int i = 0; i < 15; i++) {
                MerchOrden orden = new MerchOrden();

                // 1. Generamos valores base ficticios
                int cantidad = faker.number().numberBetween(1, 5); // Compra de 1 a 5 artículos
                int precioUnitario = faker.options().option(4000, 8500, 12000, 25000, 45000); // Precios de productos
                int precioTotal = cantidad * precioUnitario; // Cálculo automático realista

                // 2. Seteamos los campos según tu modelo
                orden.setCantidad(cantidad);
                orden.setPrecioUnitario(precioUnitario);
                orden.setPrecioTotal(precioTotal);

                // Los IDs usando rangos Long válidos para desarrollo
                orden.setMerchandiseId(faker.number().numberBetween(1L, 30L)); // IDs de productos del 1 al 30
                orden.setUserId(faker.number().numberBetween(1L, 100L));        // IDs de usuarios del 1 al 100

                // Nota: Tu método @PrePersist se encargará automáticamente
                // de setear el 'status = true' y la 'fechaOrden = LocalDateTime.now()'

                merchRepository.save(orden);
            }
            System.out.println("¡DataFaker: 15 Órdenes de Merch generadas exitosamente en perfil DEV!");
        }
    }
}