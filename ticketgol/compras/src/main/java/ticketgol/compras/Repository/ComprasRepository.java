package ticketgol.compras.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.compras.Model.Compras;
import java.util.List;

@Repository
public interface ComprasRepository extends JpaRepository<Compras, Long> {

    List<Compras> findByUsuarioId(String usuarioId);
}