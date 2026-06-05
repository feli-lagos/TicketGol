package ticketgol.pases_temporada.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.pases_temporada.model.PasesTemporada;

@Repository
public interface PasesTemporadaRepository extends JpaRepository<PasesTemporada, Long>{

}
