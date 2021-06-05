package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.CaracteristicaController;
import br.com.abce.sai.persistence.model.CaracteristicaImovel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CaracteristicaAssembler implements RepresentationModelAssembler<CaracteristicaImovel, EntityModel<CaracteristicaImovel>> {

    @Override
    public EntityModel<CaracteristicaImovel> toModel(CaracteristicaImovel entity) {
        return EntityModel.of(entity,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CaracteristicaController.class)
                        .findByOne(entity.getIdCaracteristicaImovel())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CaracteristicaController.class)
                        .findAll(entity.getDescricao(), null))
                        .withRel("caracteristicas-imovel"));
    }
}
