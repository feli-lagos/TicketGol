package ticketgol.pases_temporada.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import ticketgol.pases_temporada.model.PaseTemporada;
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

    public List<PaseTemporada> findAllPasesTemporada() {
        return ptRepository.findAll();
    }

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
    }
}