package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.CorretorController;
import br.com.abce.sai.controller.UsuarioController;
import br.com.abce.sai.persistence.model.Corretor;
import br.com.abce.sai.persistence.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CorretorAssembler implements RepresentationModelAssembler<Corretor, EntityModel<Corretor>> {

    @Override
    public EntityModel<Corretor> toModel(Corretor entity) {

        Usuario usuarioByUsuarioIdUsuario = entity.getUsuarioByUsuarioIdUsuario();

        if (usuarioByUsuarioIdUsuario != null)
            entity.setUsuarioId(usuarioByUsuarioIdUsuario.getIdUsuario());

        return EntityModel.of(entity,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CorretorController.class)
                        .findByOne(entity.getIdCorretor())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CorretorController.class)
                        .findAll(entity.getCpf(), entity.getNumCreci(), entity.getUsuarioId())).withRel("corretores"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                        .findByOne((entity.getUsuarioId()))).withRel("usuario"));
    }
}
