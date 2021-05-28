package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.TipoImovelController;
import br.com.abce.sai.persistence.model.TipoImovel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TipoImovelAssembler implements RepresentationModelAssembler<TipoImovel, EntityModel<TipoImovel>> {

    @Override
    public EntityModel<TipoImovel> toModel(TipoImovel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TipoImovelController.class).findByOne(entity.getIdTipoImovel())).withSelfRel(),
                linkTo(methodOn(TipoImovelController.class).findAll()).withRel("tipos-imovel"));
    }
}
