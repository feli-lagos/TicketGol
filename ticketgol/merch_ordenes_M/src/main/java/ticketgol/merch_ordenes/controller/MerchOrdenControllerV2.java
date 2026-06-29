package ticketgol.merch_ordenes.controller;

import ticketgol.merch_ordenes.assembler.MerchOrdenAssembler;
import ticketgol.merch_ordenes.model.MerchOrden;
import ticketgol.merch_ordenes.service.MerchOrdenService;

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
@RequestMapping("/api/v2/merch-ordenes")
public class MerchOrdenControllerV2 {

    @Autowired
    private MerchOrdenService merchOrdenService;

    @Autowired
    private MerchOrdenAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<MerchOrden>> listarOrdenes() {
        List<EntityModel<MerchOrden>> ordenes = merchOrdenService.findAllOrdenes().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ordenes,
                linkTo(methodOn(MerchOrdenControllerV2.class).listarOrdenes()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<MerchOrden> obtenerOrdenPorId(@PathVariable Long id) {
        MerchOrden orden = merchOrdenService.getOrdenById(id);
        return assembler.toModel(orden);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MerchOrden>> crearOrden(@Valid @RequestBody MerchOrden orden) {
        MerchOrden nuevaOrden = merchOrdenService.crearOrden(orden);

        return ResponseEntity
                .created(linkTo(methodOn(MerchOrdenControllerV2.class).obtenerOrdenPorId(nuevaOrden.getId())).toUri())
                .body(assembler.toModel(nuevaOrden));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MerchOrden>> actualizarOrden(@PathVariable Long id, @Valid @RequestBody MerchOrden ordenDetalles) {
        MerchOrden ordenActualizada = merchOrdenService.updateOrden(id, ordenDetalles);

        return ResponseEntity
                .ok(assembler.toModel(ordenActualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarOrden(@PathVariable Long id) {
        merchOrdenService.deleteOrden(id);

        return ResponseEntity.noContent().build();
    }
}