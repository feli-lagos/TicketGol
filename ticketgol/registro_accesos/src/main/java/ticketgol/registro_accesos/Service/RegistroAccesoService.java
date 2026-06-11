package ticketgol.registro_accesos.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.registro_accesos.Modelo.RegistroAcceso;
import ticketgol.registro_accesos.Repository.RegistroAccesoRepository;
import ticketgol.registro_accesos.Exception.RegistroAccesoNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class RegistroAccesoService {
    @Autowired
    private RegistroAccesoRepository repositorio;

    public RegistroAcceso guardarAcceso(RegistroAcceso registroAcceso) {
        if (registroAcceso.getScantime() == null) {
            registroAcceso.setScantime(LocalDateTime.now());
        }

        RegistroAcceso accesoGuardado = repositorio.save(registroAcceso);

        log.info("Acceso registrado con éxito. ID Registro: " + accesoGuardado.getId() +
                " | Procesado por Guardia ID: " + accesoGuardado.getGuardiaid() +
                " | Fecha/Hora: " + accesoGuardado.getScantime());

        return accesoGuardado;
    }

    public List<RegistroAcceso> obtenerTodos() {
        log.info("Consultando la totalidad de los registros de acceso en el sistema.");
        return repositorio.findAll();
    }

    public RegistroAcceso obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> {
                    log.warn("Intento de búsqueda fallido. El registro de acceso con ID: " + id + " no fue encontrado.");
                    return new RegistroAccesoNotFoundException("El registro de acceso con ID " + id + " no existe.");
                });
    }

    public List<RegistroAcceso> obtenerPorGuardia(Long guardiaid) {
        log.info("Obteniendo historial de accesos validados por el Guardia con ID: " + guardiaid);
        return repositorio.findByGuardiaid(guardiaid);
    }

    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            log.error("Error al eliminar. No se encontró el registro de acceso con ID: " + id);
            throw new RegistroAccesoNotFoundException("No se puede eliminar. El registro de acceso con ID " + id + " no existe.");
        }

        repositorio.deleteById(id);
        log.info("El registro de acceso con ID: " + id + " ha sido eliminado permanentemente del sistema.");
    }
}