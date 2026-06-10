package ticketgol.merch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.merch.model.Merchandise;

@Repository
public interface MerchandiseRepository extends JpaRepository<Merchandise, Long> {
    // Al extender de JpaRepository, Spring Data JPA genera automáticamente
    // todos los métodos de persistencia básicos (save, findById, findAll, delete, etc.).
}