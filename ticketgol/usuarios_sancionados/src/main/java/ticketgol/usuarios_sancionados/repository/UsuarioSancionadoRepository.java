package ticketgol.usuarios_sancionados.repository;

import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioSancionadoRepository extends JpaRepository<UsuarioSancionado, Long> {

    // JPQL
    @Query("SELECT u FROM UsuarioSancionado u WHERE u.rut = :rut")
    UsuarioSancionado buscarPorRut(@Param("rut") String rut);

    // SQL nativo
    @Query(value = "SELECT * FROM usuarios_sancionados WHERE motivo = :motivo", nativeQuery = true)
    List<UsuarioSancionado> buscarPorMotivo(@Param("motivo") String motivo);

    UsuarioSancionado findByRut(String rut);

    List<UsuarioSancionado> findByMotivo(String motivo);

    List<UsuarioSancionado> findByFechaSancion(java.util.Date fechaSancion);

    List<UsuarioSancionado> findByFechaExpiracion(java.util.Date fechaExpiracion);
}