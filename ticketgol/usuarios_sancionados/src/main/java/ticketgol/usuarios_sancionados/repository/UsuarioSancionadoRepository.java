package ticketgol.usuarios_sancionados.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.usuarios_sancionados.model.UsuarioSancionado;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsuarioSancionadoRepository extends JpaRepository<UsuarioSancionado, Long> {

    UsuarioSancionado findByRut(String rut);

    List<UsuarioSancionado> findByMotivo(String motivo);

    List<UsuarioSancionado> findByFechaSancion(LocalDate fechaSancion);

    List<UsuarioSancionado> findByFechaExpiracion(LocalDate fechaExpiracion);

    boolean existsByRut(String rut);
}