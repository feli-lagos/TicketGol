package ticketgol.clubes.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ticketgol.clubes.controller.ClubControllerV2;
import ticketgol.clubes.dto.ClubDTO;

@Component
public class ClubesModelAssembler implements RepresentationModelAssembler<ClubDTO, EntityModel<ClubDTO>> {

    @Override
    public EntityModel<ClubDTO> toModel(ClubDTO clubDto) {
        return EntityModel.of(clubDto,
                // Enlace al recurso individual (self)
                linkTo(methodOn(ClubControllerV2.class).buscarPorId(clubDto.getId())).withSelfRel(),
                // Enlace a la colección completa
                linkTo(methodOn(ClubControllerV2.class).listarClubes()).withRel("clubes"));
    }
}