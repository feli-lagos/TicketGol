package ticketgol.eventos.controller;

import ticketgol.eventos.assembler.EventosModelAssembler;
import ticketgol.eventos.dto.EventoDTO;
import ticketgol.eventos.service.EventoService;

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
@RequestMapping("/api/v2/eventos")
public class EventoControllerV2 {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private EventosModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<EventoDTO>> listarEventos() {
        List<EntityModel<EventoDTO>> eventos = eventoService.obtenerTodosLosEventos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(eventos,
                linkTo(methodOn(EventoControllerV2.class).listarEventos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<EventoDTO> buscarPorId(@PathVariable Long id) {
        EventoDTO evento = eventoService.buscarPorId(id);
        return assembler.toModel(evento);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventoDTO>> crearEvento(@Valid @RequestBody EventoDTO evento) {
        EventoDTO nuevoEvento = eventoService.guardarEvento(evento);

        return ResponseEntity
                .created(linkTo(methodOn(EventoControllerV2.class).buscarPorId(nuevoEvento.getId())).toUri())
                .body(assembler.toModel(nuevoEvento));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EventoDTO>> actualizarEvento(@PathVariable Long id, @Valid @RequestBody EventoDTO evento) {
        EventoDTO eventoActualizado = eventoService.actualizarEvento(id, evento);

        return ResponseEntity
                .ok(assembler.toModel(eventoActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> eliminarEvento(@PathVariable Long id) {
        eventoService.eliminarEvento(id);
        return ResponseEntity.noContent().build();
    }
}