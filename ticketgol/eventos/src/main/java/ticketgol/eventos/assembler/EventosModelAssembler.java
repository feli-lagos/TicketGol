package ticketgol.eventos.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ticketgol.eventos.controller.EventoControllerV2;
import ticketgol.eventos.dto.EventoDTO;

@Component
public class EventosModelAssembler implements RepresentationModelAssembler<EventoDTO, EntityModel<EventoDTO>> {

    @Override
    public EntityModel<EventoDTO> toModel(EventoDTO eventoDto) {
        return EntityModel.of(eventoDto,
                linkTo(methodOn(EventoControllerV2.class).buscarPorId(eventoDto.getId())).withSelfRel(),
                linkTo(methodOn(EventoControllerV2.class).listarEventos()).withRel("eventos"));
    }
}