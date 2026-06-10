package ticketgol.pases_temporada.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ticketgol.pases_temporada.model.PaseTemporada;
import ticketgol.pases_temporada.model.PaseTemporadaDtoFront;
import ticketgol.pases_temporada.service.PaseTemporadaService;
import java.util.List;

@Controller
@RestController
@RequestMapping("api/v1/ticketgol/pasestemporada")
public class PaseTemporadaController {

    @Autowired
    private PaseTemporadaService ptService;


    //------------------------------------------------ SECCION READ ------------------------------------------------------------------------------------
    @GetMapping("/list")
    public ResponseEntity<List<PaseTemporada>> listPaseTemporadas() {
        List<PaseTemporada> listPaseTemporadas = ptService.findAllPasesTemporada();
        if(listPaseTemporadas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listPaseTemporadas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaseTemporada> getPaseTemporada(@PathVariable int id) {
        PaseTemporada paseTemporada = ptService.findPaseTemporadaById(id);
        return ResponseEntity.ok(paseTemporada);
    }


    //----------------------------------------------------- SECCION CREATE --------------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<PaseTemporada> createPaseTemporada(@RequestBody PaseTemporada pt) {
        PaseTemporada paseNuevo = ptService.savePaseTemporada(pt);
        return ResponseEntity.status(HttpStatus.CREATED).body(paseNuevo);
    }


    //----------------------------------------------------- SECCION UPDATE -------------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<PaseTemporadaDtoFront> updatePaseTemporada(@PathVariable Long id, @Valid @RequestBody PaseTemporadaDtoFront ptDtoFront) {
        PaseTemporadaDtoFront paseNuevo = ptService.updatePaseTemporadaFromDto(id, ptDtoFront);
        return ResponseEntity.ok(paseNuevo);
    }


    //----------------------------------------------------- SECCION DELETE -------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaseTemporada(@PathVariable Long id) {
        String mensajeConfirmacion = ptService.deletePaseTemporada(id);
        return ResponseEntity.ok(mensajeConfirmacion);
    }
}
