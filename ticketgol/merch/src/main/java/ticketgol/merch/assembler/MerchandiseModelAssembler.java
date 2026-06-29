package ticketgol.merch.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ticketgol.merch.controller.MerchandiseControllerV2;
import ticketgol.merch.dto.MerchandiseDTO;

@Component
public class MerchandiseModelAssembler implements RepresentationModelAssembler<MerchandiseDTO, EntityModel<MerchandiseDTO>> {

    @Override
    public EntityModel<MerchandiseDTO> toModel(MerchandiseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(MerchandiseControllerV2.class).buscarPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(MerchandiseControllerV2.class).listarArticulos()).withRel("merchandise"));
    }
}