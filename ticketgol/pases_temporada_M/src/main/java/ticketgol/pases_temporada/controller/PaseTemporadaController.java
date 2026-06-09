package ticketgol.pases_temporada.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.service.PaseTemporadaService;

@RestController
@RequestMapping("/api/v1/pases-temporada")
public class PaseTemporadaController {

    @Autowired
    private PaseTemporadaService ptService;

    @PostMapping
    public Mono<ResponseEntity<PaseTemporada>> crear(@Valid @RequestBody PaseTemporada pt) {
        return ptService.savePaseTemporada(pt)
                .map(nuevoPase -> ResponseEntity.status(HttpStatus.CREATED).body(nuevoPase));
    }

    @GetMapping
    public Flux<PaseTemporada> listarTodos() {
        return Flux.fromIterable(ptService.findAllPasesTemporada());
    }
}