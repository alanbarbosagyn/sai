package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.PerfilImovelController;
import br.com.abce.sai.persistence.model.PerfilImovel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PerfilImovelAssembler implements RepresentationModelAssembler<PerfilImovel, EntityModel<PerfilImovel>> {
    @Override
    public EntityModel<PerfilImovel> toModel(PerfilImovel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PerfilImovelController.class).findByOne(entity.getIdPerfilImovel())).withSelfRel(),
                linkTo(methodOn(PerfilImovelController.class).findAll()).withRel("perfis-imovel"));
    }
}
