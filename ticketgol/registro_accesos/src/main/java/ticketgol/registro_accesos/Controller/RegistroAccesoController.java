package ticketgol.registro_accesos.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.registro_accesos.Dto.RegistroAccesoRequestDTO;
import ticketgol.registro_accesos.Dto.RegistroAccesoResponseDTO;
import ticketgol.registro_accesos.Modelo.RegistroAcceso;
import ticketgol.registro_accesos.Service.RegistroAccesoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registro-accesos")
public class RegistroAccesoController {

    @Autowired
    private RegistroAccesoService service;

    @GetMapping
    public List<RegistroAccesoResponseDTO> listarAccesos() {
        return service.obtenerTodos()
                .stream()
                .map(this::convertirAEntityADtoResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroAccesoResponseDTO> buscarPorId(@PathVariable Long id) {
        RegistroAcceso acceso = service.obtenerPorId(id);
        return ResponseEntity.ok(convertirAEntityADtoResponse(acceso));
    }

    @GetMapping("/guardia/{guardiaid}")
    public List<RegistroAccesoResponseDTO> listarPorGuardia(@PathVariable Long guardiaid) {
        return service.obtenerPorGuardia(guardiaid)
                .stream()
                .map(this::convertirAEntityADtoResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<RegistroAccesoResponseDTO> crearAcceso(@Valid @RequestBody RegistroAccesoRequestDTO requestDTO) {

        RegistroAcceso accesoEntity = convertirADtoRequestAEntity(requestDTO);
        RegistroAcceso nuevoAcceso = service.guardarAcceso(accesoEntity);

        return new ResponseEntity<>(convertirAEntityADtoResponse(nuevoAcceso), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAcceso(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private RegistroAccesoResponseDTO convertirAEntityADtoResponse(RegistroAcceso acceso) {
        RegistroAccesoResponseDTO response = new RegistroAccesoResponseDTO();
        response.setId(acceso.getId());
        response.setComprasid(acceso.getComprasid());
        response.setAccessgranted(acceso.getAccessgranted());
        response.setGuardiaid(acceso.getGuardiaid());
        response.setScantime(acceso.getScantime());
        return response;
    }
    private RegistroAcceso convertirADtoRequestAEntity(RegistroAccesoRequestDTO dto) {
        RegistroAcceso acceso = new RegistroAcceso();
        acceso.setComprasid(dto.getComprasid());
        acceso.setAccessgranted(dto.getAccessgranted());
        acceso.setGuardiaid(dto.getGuardiaid());
        // Nota: scantime no se setea aquí, el Service lo pondrá automáticamente al guardar
        return acceso;
    }
}