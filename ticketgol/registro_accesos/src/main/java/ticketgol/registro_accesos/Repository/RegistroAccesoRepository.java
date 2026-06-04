package ticketgol.registro_accesos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  org.springframework.stereotype.Repository;
import ticketgol.registro_accesos.Modelo.RegistroAcceso;
import java.util.List;

@Repository
public interface RegistroAccesoRepository extends  JpaRepository<RegistroAcceso,Long>{
    List<RegistroAcceso> findByGuardiaid(Long guardiaid);

    List<RegistroAcceso> findByComprasid(Long comprasid);

}
