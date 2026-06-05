package ticketgol.usuarios_sancionados.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ticketgol.usuarios_sancionados.model.UsuarioSancionado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UsuarioSancionadoRepository extends JpaRepository<UsuarioSancionado, Long> {

    UsuarioSancionado findByRut(String rut);

    List<UsuarioSancionado> findByMotivo(String motivo);

    List<UsuarioSancionado> findByFechaSancion(LocalDate fechaSancion);

    List<UsuarioSancionado> findByFechaExpiracion(LocalDate fechaExpiracion);

    boolean existsByRut(@NotBlank(message = "El RUT es obligatorio") @Size(min = 7, max = 13, message = "El RUT debe tener entre 7 y 13 caracteres") String rut);
}