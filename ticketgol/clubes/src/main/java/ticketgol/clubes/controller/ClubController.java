package ticketgol.clubes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.clubes.dto.ClubDTO;
import ticketgol.clubes.model.Club;
import ticketgol.clubes.service.ClubServices;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/clubes")
@Tag(name = "Clubes", description = "Operaciones relacionadas a los clubes")

public class ClubController {

    private final ClubServices clubServices;

    public ClubController(ClubServices clubServices) {
        this.clubServices = clubServices;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clubes", description = "Obtiene una lista de todos los clubes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    })
    public ResponseEntity<List<ClubDTO>> obtenerTodos() {
        log.info("Recibida petición GET para listar todos los clubes");
        return ResponseEntity.ok(clubServices.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener clubes por ID", description = "Obtiene el club específico a través del ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Carrera no encontrada")
    })
    public ResponseEntity<ClubDTO> buscarPorId(@PathVariable Long id) {
        log.info("Recibida petición GET para buscar el club con ID: {}", id);
        return ResponseEntity.ok(clubServices.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un club nuevo", description = "Crea un nuevo club en el registro de Clubes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Club no registrado")
    })
    public ResponseEntity<ClubDTO> guardarClub(@Valid @RequestBody ClubDTO clubDto) {
        log.info("Recibida petición POST para registrar un nuevo club");
        ClubDTO nuevoClub = clubServices.guardarClub(clubDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoClub);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un club existente", description = "Modifica los datos de un club a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Club actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Club no encontrado")
    })
    public ResponseEntity<ClubDTO> actualizarClub(@PathVariable Long id, @Valid @RequestBody ClubDTO clubDto) {
        log.info("Recibida petición PUT para actualizar el club con ID: {}", id);
        ClubDTO clubActualizado = clubServices.actualizarClub(id, clubDto);
        return ResponseEntity.ok(clubActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un club", description = "Elimina un club del registro a través de su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Club eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Club no encontrado")
    })
    public ResponseEntity<String> eliminarClub(@PathVariable Long id) {
        log.info("Recibida petición DELETE para eliminar el club con ID: {}", id);
        clubServices.eliminarClub(id);
        return ResponseEntity.ok("Club eliminado correctamente");
    }

}