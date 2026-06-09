package ticketgol.pases_temporada.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.service.PaseTemporadaService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@RequestMapping(api/v1/ticket)
public class PaseTemporadaController {

    @Autowired
    private PaseTemporadaService ptService;

    @GetMapping
    public ResponseEntity<List<PaseTemporada>> listPaseTemporadas() {
        List<PaseTemporada> listPaseTemporadas = ptService.findAllPasesTemporada();
        if(listPaseTemporadas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listPaseTemporadas);
    }


}
