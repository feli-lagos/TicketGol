package ticketgol.guardias.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.guardias.Dto.GuardiaRequestDTO;
import ticketgol.guardias.Dto.GuardiaResponseDTO;
import ticketgol.guardias.model.Guardias;
import ticketgol.guardias.service.GuardiasService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guardias")
public class GuardiasController {

    @Autowired
    private GuardiasService service;

    @GetMapping
    public List<GuardiaResponseDTO> listarGuardias() {
        return service.obtenerTodos()
                .stream()
                .map(this::convertirAEntityADtoResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuardiaResponseDTO> buscarPorId(@PathVariable Long id) {
        Guardias guardia = service.obtenerPorId(id);
        return ResponseEntity.ok(convertirAEntityADtoResponse(guardia));
    }

    @GetMapping("/estadio/{estadioId}")
    public List<GuardiaResponseDTO> listarPorEstadio(@PathVariable Long estadioId) {
        return service.obtenerPorEstadio(estadioId)
                .stream()
                .map(this::convertirAEntityADtoResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<GuardiaResponseDTO> crearGuardia(@Valid @RequestBody GuardiaRequestDTO requestDTO) {

        // Convertimos el DTO de entrada a Entidad para que el servicio la procese
        Guardias guardiaEntity = convertirADtoRequestAEntity(requestDTO);
        Guardias nuevoGuardia = service.guardar(guardiaEntity);

        // Retornamos el DTO de salida limpio
        return new ResponseEntity<>(convertirAEntityADtoResponse(nuevoGuardia), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGuardia(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private GuardiaResponseDTO convertirAEntityADtoResponse(Guardias guardia) {
        GuardiaResponseDTO response = new GuardiaResponseDTO();
        response.setId(guardia.getId());
        response.setEstadioId(guardia.getEstadioId());
        response.setNombre(guardia.getNombre());
        response.setNumeroPlaca(guardia.getNumeroPlaca());
        return response;
    }

    private Guardias convertirADtoRequestAEntity(GuardiaRequestDTO dto) {
        Guardias guardia = new Guardias();
        guardia.setEstadioId(dto.getEstadioId());
        guardia.setNombre(dto.getNombre());
        guardia.setNumeroPlaca(dto.getNumeroPlaca());
        return guardia;
    }
}