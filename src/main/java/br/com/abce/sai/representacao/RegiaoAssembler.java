package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.RegiaoController;
import br.com.abce.sai.persistence.model.Regiao;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegiaoAssembler implements RepresentationModelAssembler<Regiao, EntityModel<Regiao>> {

    @Override
    public EntityModel<Regiao> toModel(Regiao entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(RegiaoController.class).findByOne(entity.getIdRegiao())).withSelfRel(),
                linkTo(methodOn(RegiaoController.class).findAll(entity.getMunicipio().getIdMunicipio(), entity.getMunicipio().getCodgIbge())).withRel("regiao"));
    }
}
