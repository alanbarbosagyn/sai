package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.UsuarioController;
import br.com.abce.sai.persistence.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class UsuarioAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UsuarioController.class).findByOne(entity.getIdUsuario())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).findAll()).withRel("usuarios"));
    }
}
