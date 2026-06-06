package ticketgol.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ticketgol.usuarios.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByRut(String rut);

    Usuario findByCorreo(String correo);

    boolean existsByRut(String rut);

    boolean existsByCorreo(String correo);
}