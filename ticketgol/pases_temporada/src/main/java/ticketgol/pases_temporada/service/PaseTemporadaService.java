package ticketgol.pases_temporada.service;


import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ticketgol.pases_temporada.exception.PassNotFoundException;
import ticketgol.pases_temporada.exception.UserSanctionedException;
import ticketgol.pases_temporada.mapper.PaseMapper;
import ticketgol.pases_temporada.mapper.UsuarioMapper;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.PaseTemporadaDtoFront;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;
import ticketgol.pases_temporada.repository.PaseTemporadaRepository;
import ticketgol.pases_temporada.webclient.PaseUsuario;

import java.util.List;

@Transactional
@Service
public class PaseTemporadaService {

    @Autowired
    private PaseTemporadaRepository ptRepository;

    @Autowired
    private PaseUsuario paseUsuario;

    @Autowired
    private UsuarioMapper userMapper;

    @Autowired
    private PaseMapper paseMapper;

    private static final Logger log = LoggerFactory.getLogger(PaseTemporadaService.class.getName());



    //-------------------------------------SECCION READ------------------------------------------------

    public List<PaseTemporada> findAllPasesTemporada() {
        return ptRepository.findAll();
    }

    public PaseTemporada findPaseTemporadaById(long id) {
        log.info("buscando pase con id: {}", id);
        return ptRepository.findById(id)
                .orElseThrow(() -> new PassNotFoundException("el pase con id: " + id + " no fue encontrado"));
    }

    //----------------------------------- SECCION CREATE ------------------------------------------------

    //guardar pase de temporada, conexión con webclient trayendo usuario convertido a Dto
    public PaseTemporada savePaseTemporada(PaseTemporada pt) {
        UsuarioEstadoDto usuarioEstadoDto = paseUsuario.getUsuarioDtoById(pt.getUserId());
        if ("SANCIONADO".equals(usuarioEstadoDto.getEstadoSancion())){
            throw new UserSanctionedException("usuario rut: " + usuarioEstadoDto.getRut() + " se encuentra sancionado");
        }
        log.info("Pase creado para el usuario con rut: " + usuarioEstadoDto.getRut());
        return ptRepository.save(pt);
    }

    //----------------------------------- SECCION UPDATE ------------------------------------------------

    @Transactional
    public PaseTemporadaDtoFront updatePaseTemporadaFromDto(Long id, PaseTemporadaDtoFront dtoFront) {
        PaseTemporada paseReal = ptRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pase no se pudo actualizar porque no existe");
                    return new PassNotFoundException("el pase con id: " + id + " no fue encontrado");
                });
        paseMapper.updatePassFromDto(dtoFront, paseReal);
        PaseTemporada paseActualizado = ptRepository.save(paseReal);
        log.info("Actualizando pase con id: {}", id);
        return  dtoFront;
    }

    //--------------------------------- SECCION DELETE ---------------------------------------------------

    public String deletePaseTemporada(long id) {
        ptRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pase con id: " + id + " no se pudo eliminar porque no existe");
                    return new PassNotFoundException("el pase con id: " + id + " no se puede eliminar porque no existe");
                });
        ptRepository.deleteById(id);
        return "el pase con id: " + id + " fue eliminado";
    }









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

}