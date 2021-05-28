package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.ImovelController;
import br.com.abce.sai.persistence.model.Imovel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImovelModelAssembler implements RepresentationModelAssembler<Imovel, EntityModel<Imovel>> {

    @Override
    public EntityModel<Imovel> toModel(Imovel entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ImovelController.class).findByOne(entity.getIdImovel())).withSelfRel(),
                linkTo(methodOn(ImovelController.class).findAll()).withRel("imoveis"));
    }
}
