package ticketgol.registro_accesos.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.registro_accesos.Modelo.RegistroAcceso;
import ticketgol.registro_accesos.Repository.RegistroAccesoRepository;
import ticketgol.registro_accesos.Exception.RegistroAccesoNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroAccesoService {
    @Autowired
    private RegistroAccesoRepository repositorio;

    public RegistroAcceso guardarAcceso(RegistroAcceso registroAcceso) {
        if (registroAcceso.getScantime() == null) {
            registroAcceso.setScantime(LocalDateTime.now());
        }
        return repositorio.save(registroAcceso);
    }

    public List<RegistroAcceso> obtenerTodos() {
        return repositorio.findAll();
    }

    public RegistroAcceso obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RegistroAccesoNotFoundException("El registro de acceso con ID " + id + " no existe."));
    }

    public List<RegistroAcceso> obtenerPorGuardia(Long guardiaid) {
        return repositorio.findByGuardiaid(guardiaid);
    }

    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RegistroAccesoNotFoundException("No se puede eliminar. El registro de acceso con ID " + id + " no existe.");
        }
        repositorio.deleteById(id);
    }
}