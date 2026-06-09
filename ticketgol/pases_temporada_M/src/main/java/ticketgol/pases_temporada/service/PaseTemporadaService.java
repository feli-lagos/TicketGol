package ticketgol.pases_temporada.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import ticketgol.pases_temporada.exception.UserSanctionedException;
import ticketgol.pases_temporada.mapper.UsuarioMapper;
=======
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
>>>>>>> main
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.UsuarioEstadoDto;
import ticketgol.pases_temporada.repository.PaseTemporadaRepository;
import ticketgol.pases_temporada.webclient.PaseUsuario;

import java.time.LocalDate;
import java.util.List;
<<<<<<< HEAD
import java.util.Map;
=======
>>>>>>> main

@Transactional
@Service
public class PaseTemporadaService {

    @Autowired
    private PaseTemporadaRepository ptRepository;

    @Autowired
    private PaseUsuario paseUsuario;
<<<<<<< HEAD

    @Autowired
    private UsuarioMapper userMapper;
=======
>>>>>>> main

    public List<PaseTemporada> findAllPasesTemporada() {
        return ptRepository.findAll();
    }

<<<<<<< HEAD
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
=======
    public Mono<PaseTemporada> savePaseTemporada(PaseTemporada pt) {
        return paseUsuario.getUsuarioByRut(pt.getUserRut())
                .flatMap(usuario -> {
                    if ("SANCIONADO".equals(usuario.get("estadoSancion"))) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "No se puede emitir el pase de temporada: El usuario se encuentra SANCIONADO."
                        ));
                    }
                    return Mono.just(ptRepository.save(pt));
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No se puede emitir el pase: El RUT ingresado no existe en el sistema de usuarios."
                        ));
                    }
                    return Mono.error(new ResponseStatusException(
                            ex.getStatusCode(),
                            ex.getResponseBodyAsString()
                    ));
                });
>>>>>>> main
    }
}