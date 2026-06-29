package ticketgol.merch.controller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ticketgol.merch.assembler.MerchandiseModelAssembler;
import ticketgol.merch.dto.MerchandiseDTO;
import ticketgol.merch.service.MerchandiseService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping(value = "/api/v2/merchandise", produces = MediaTypes.HAL_JSON_VALUE)
public class MerchandiseControllerV2 {

    private final MerchandiseService service;
    private final MerchandiseModelAssembler assembler;

    public MerchandiseControllerV2(MerchandiseService service, MerchandiseModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<MerchandiseDTO>> listarArticulos() {
        List<EntityModel<MerchandiseDTO>> articulos = service.obtenerTodosLosArticulos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(articulos,
                linkTo(methodOn(MerchandiseControllerV2.class).listarArticulos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<MerchandiseDTO> buscarPorId(@PathVariable Long id) {
        MerchandiseDTO articulo = service.buscarPorId(id);
        return assembler.toModel(articulo);
    }

    @PostMapping
    public ResponseEntity<EntityModel<MerchandiseDTO>> registrarArticulo(@Valid @RequestBody MerchandiseDTO dto) {
        MerchandiseDTO nuevoArticulo = service.guardarArticulo(dto);

        return ResponseEntity
                .created(linkTo(methodOn(MerchandiseControllerV2.class).buscarPorId(nuevoArticulo.getId())).toUri())
                .body(assembler.toModel(nuevoArticulo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<MerchandiseDTO>> actualizarArticulo(@PathVariable Long id, @Valid @RequestBody MerchandiseDTO dto) {
        MerchandiseDTO articuloActualizado = service.actualizarArticulo(id, dto);

        return ResponseEntity.ok(assembler.toModel(articuloActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticulo(@PathVariable Long id) {
        service.eliminarArticulo(id);

        return ResponseEntity.noContent().build();
    }
}