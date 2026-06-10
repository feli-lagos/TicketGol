package ticketgol.pases_temporada.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.pases_temporada.model.PaseTemporada;

@Repository
public interface PaseTemporadaRepository extends JpaRepository<PaseTemporada, Long>{

}