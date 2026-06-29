package ticketgol.clubes.controller;

import ticketgol.clubes.assemblers.ClubesModelAssembler;
import ticketgol.clubes.dto.ClubDTO;
import ticketgol.clubes.service.ClubServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/clubes")
public class ClubControllerV2 {

    @Autowired
    private ClubServices clubService;

    @Autowired
    private ClubesModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ClubDTO>> listarClubes() {
        List<EntityModel<ClubDTO>> clubes = clubService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clubes,
                linkTo(methodOn(ClubControllerV2.class).listarClubes()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ClubDTO> buscarPorId(@PathVariable Long id) {
        ClubDTO club = clubService.buscarPorId(id);
        return assembler.toModel(club);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClubDTO>> crearClub(@Valid @RequestBody ClubDTO clubDto) {
        ClubDTO nuevoClub = clubService.guardarClub(clubDto);

        return ResponseEntity
                .created(linkTo(methodOn(ClubControllerV2.class).buscarPorId(nuevoClub.getId())).toUri())
                .body(assembler.toModel(nuevoClub));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClubDTO>> actualizarClub(@PathVariable Long id, @Valid @RequestBody ClubDTO clubDto) {
        ClubDTO clubActualizado = clubService.actualizarClub(id, clubDto);

        return ResponseEntity
                .ok(assembler.toModel(clubActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarClub(@PathVariable Long id) {
        clubService.eliminarClub(id);

        return ResponseEntity.noContent().build();
    }
}