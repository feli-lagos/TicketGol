package ticketgol.pases_temporada.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.repository.PaseTemporadaRepository;
import ticketgol.pases_temporada.webclient.PaseUsuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
@Service
public class PaseTemporadaService {

    @Autowired
    private PaseTemporadaRepository ptRepository;

    @Autowired
    private PaseUsuario paseUsuario;

    public List<PaseTemporada> findAllPasesTemporada() {
        return ptRepository.findAll();
    }

    private static final Logger log = LoggerFactory.getLogger(PaseTemporadaService.class.getName());

    //Método en caso de tener que buscar en la lista de sancionados
    private boolean tieneSancionVigente(Map<String, Object> usuario) {
        if (usuario == null || usuario.isEmpty()) {
            return false;
        }
        LocalDate fechaExpiracion = LocalDate.parse(usuario.get("fecha_expiracion").toString());
        LocalDate fechaActual = LocalDate.now();
        if (fechaActual.isAfter(fechaExpiracion)) {
            return false;
        } else {
            return true;
        }
    }

    public PaseTemporada savePaseTemporada(PaseTemporada pt) {
        Map<String, Object> usuarioSancionado = paseUsuario.getUsuarioById(pt.getId());
        if (usuarioSancionado == null || usuarioSancionado.isEmpty()) {
            throw new UsuarioNotFoundException("el usuario no existe en la lista de usuarios");
        }
            log.info("pase creado para el usuario {}", usuarioSancionado.get().get());
            return ptRepository.save(pt);
    }

}

