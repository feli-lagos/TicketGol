package ticketgol.clubes.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.clubes.dto.ClubDTO;
import ticketgol.clubes.service.ClubServices;

import java.util.List;

@RestController
@RequestMapping("/api/clubes")
public class ClubController {

    private final ClubServices clubServices;

    public ClubController(ClubServices clubServices) {
        this.clubServices = clubServices;
    }

    @GetMapping
    public ResponseEntity<List<ClubDTO>> obtenerTodos() {
        return ResponseEntity.ok(clubServices.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clubServices.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClubDTO> guardarClub(@Valid @RequestBody ClubDTO clubDto) {
        ClubDTO nuevoClub = clubServices.guardarClub(clubDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoClub);
    }
}