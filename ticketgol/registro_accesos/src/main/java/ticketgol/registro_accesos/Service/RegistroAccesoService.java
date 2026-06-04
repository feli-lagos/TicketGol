package ticketgol.registro_accesos.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.registro_accesos.Modelo.RegistroAcceso;
import ticketgol.registro_accesos.Repository.RegistroAccesoRepository; // <-- Importa tu repositorio en inglés

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RegistroAccesoService {
    @Autowired
    private RegistroAccesoRepository repositorio; // <-- Conectado a tu interfaz de repositorio

    public RegistroAcceso guardarAcceso(RegistroAcceso registroAcceso) {
        if (registroAcceso.getScantime() == null) {
            registroAcceso.setScantime(LocalDateTime.now());
        }
        return repositorio.save(registroAcceso);
    }

    // 2. Obtener todo el historial de accesos
    public List<RegistroAcceso> obtenerTodos() {
        return repositorio.findAll();
    }

    public Optional<RegistroAcceso> obtenerPorId(Long id) {
        return repositorio.findById(id);
    }

    public List<RegistroAcceso> obtenerPorGuardia(Long guardiaid) {
        return repositorio.findByGuardiaid(guardiaid);
    }

    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }

}
