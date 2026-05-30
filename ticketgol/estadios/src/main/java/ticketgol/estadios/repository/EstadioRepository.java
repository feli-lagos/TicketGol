package ticketgol.estadios.repository;

import ticketgol.estadios.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    // JPQL
    @Query("SELECT e FROM Estadio e WHERE e.nombre = :nombre")
    Estadio buscarPorNombre(@Param("nombre") String nombre);

    // SQL nativo
    @Query(value = "SELECT * FROM estadios WHERE ciudad = :ciudad", nativeQuery = true)
    List<Estadio> buscarPorCiudad(@Param("ciudad") String ciudad);

    // Query methods (Spring Data JPA)
    List<Estadio> findByNombre(String nombre);

    List<Estadio> findByCiudad(String ciudad);

    List<Estadio> findByCapacidad(Integer capacidad);

}