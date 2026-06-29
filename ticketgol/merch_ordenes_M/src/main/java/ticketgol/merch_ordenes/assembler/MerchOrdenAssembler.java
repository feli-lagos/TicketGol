package ticketgol.merch_ordenes.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ticketgol.merch_ordenes.controller.MerchOrdenControllerV2;
import ticketgol.merch_ordenes.model.MerchOrden;

@Component
public class MerchOrdenAssembler implements RepresentationModelAssembler<MerchOrden, EntityModel<MerchOrden>> {

    @Override
    public EntityModel<MerchOrden> toModel(MerchOrden orden) {
        return EntityModel.of(orden,
                // Link individual (self) -> /api/v2/merch-ordenes/{id}
                linkTo(methodOn(MerchOrdenControllerV2.class).obtenerOrdenPorId(orden.getId())).withSelfRel(),
                // Link a la colección completa -> /api/v2/merch-ordenes
                linkTo(methodOn(MerchOrdenControllerV2.class).listarOrdenes()).withRel("merch_ordenes"));
    }
}