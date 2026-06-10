package ticketgol.clubes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ticketgol.clubes.model.Club;

public interface ClubRepository extends JpaRepository<Club, Long>{
}
