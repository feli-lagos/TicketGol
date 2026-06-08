package ticketgol.guardias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.guardias.model.Guardias;

import java.util.List;

@Repository
public interface GuardiasRepository extends JpaRepository<Guardias, Long> {


    List<Guardias> findByEstadioId(Long estadioId);
}
