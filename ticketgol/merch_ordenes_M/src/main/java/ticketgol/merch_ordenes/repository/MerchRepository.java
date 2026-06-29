package ticketgol.merch_ordenes.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.merch_ordenes.model.MerchOrden;

@Repository
public interface MerchRepository extends JpaRepository<MerchOrden, Long> {
}
