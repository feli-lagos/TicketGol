package ticketgol.pases_temporada.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.pases_temporada.exception.UserSanctionedException;
import ticketgol.pases_temporada.mapper.UsuarioMapper;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;
import ticketgol.pases_temporada.repository.PaseTemporadaRepository;
import ticketgol.pases_temporada.webclient.PaseUsuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class PaseTemporadaService {

    @Autowired
    private PaseTemporadaRepository ptRepository;

    @Autowired
    private PaseUsuario paseUsuario;

    @Autowired
    private UsuarioMapper userMapper;

    public List<PaseTemporada> findAllPasesTemporada() {
        return ptRepository.findAll();
    }

    private static final Logger log = LoggerFactory.getLogger(PaseTemporadaService.class.getName());

    /* metodo en caso de tener que buscar en la lista de sancionados
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
    }*/


    //guardar pase de temporada, conexión con webclient trayendo usuario convertido a Dto
    public PaseTemporada savePaseTemporada(PaseTemporada pt) {
        UsuarioEstadoDto usuarioEstadoDto = paseUsuario.getUsuarioDtoById(pt.getUserId());
        if ("SANCIONADO".equals(usuarioEstadoDto.getEstadoSancion())){
            throw new UserSanctionedException("usuario rut: " + usuarioEstadoDto.getRut() + "se encuentra sancionado");
        }
        log.info("Pase creado para el usuario con rut: " + usuarioEstadoDto.getRut());
        return ptRepository.save(pt);
    }


}

