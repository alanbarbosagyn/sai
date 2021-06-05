package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.ConstrutorController;
import br.com.abce.sai.persistence.model.Conveniencia;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class ConvenienciaAssembler implements RepresentationModelAssembler<Conveniencia, EntityModel<Conveniencia>> {

    @Override
    public EntityModel<Conveniencia> toModel(Conveniencia entity) {
        return EntityModel.of(entity,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConstrutorController.class).findByOne(entity.getIdConveniencia())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConstrutorController.class).findAll(entity.getDescricao())).withRel("conveniencias-imovel"));
    }
}