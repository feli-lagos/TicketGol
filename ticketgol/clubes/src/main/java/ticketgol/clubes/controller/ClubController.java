package ticketgol.clubes.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.clubes.dto.ClubDTO;
import ticketgol.clubes.service.ClubServices;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clubes")
public class ClubController {

    private final ClubServices clubServices;

    public ClubController(ClubServices clubServices) {
        this.clubServices = clubServices;
    }

    @GetMapping
    public ResponseEntity<List<ClubDTO>> obtenerTodos() {
        log.info("Recibida petición GET para listar todos los clubes");
        return ResponseEntity.ok(clubServices.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> buscarPorId(@PathVariable Long id) {
        log.info("Recibida petición GET para buscar el club con ID: {}", id);
        return ResponseEntity.ok(clubServices.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClubDTO> guardarClub(@Valid @RequestBody ClubDTO clubDto) {
        log.info("Recibida petición POST para registrar un nuevo club");
        ClubDTO nuevoClub = clubServices.guardarClub(clubDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoClub);
    }
}