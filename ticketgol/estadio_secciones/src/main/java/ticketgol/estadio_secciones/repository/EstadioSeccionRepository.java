package ticketgol.estadio_secciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.estadio_secciones.model.EstadioSeccion;

import java.util.List;

@Repository
public interface EstadioSeccionRepository extends JpaRepository<EstadioSeccion, Long> {
    List<EstadioSeccion> findByEstadioId(Long estadioId);
}