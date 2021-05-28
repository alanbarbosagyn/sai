package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.CorretorController;
import br.com.abce.sai.persistence.model.Corretor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CorretorAssembler implements RepresentationModelAssembler<Corretor, EntityModel<Corretor>> {

    @Override
    public EntityModel<Corretor> toModel(Corretor entity) {
        return EntityModel.of(entity,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CorretorController.class)
                        .findByOne(entity.getIdCorretor())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CorretorController.class)
                        .findAll(entity.getCpf(), entity.getNumCreci())).withRel("corretores"));
    }
}
