package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.ConstrutorController;
import br.com.abce.sai.controller.UsuarioController;
import br.com.abce.sai.dto.ConstrutorDto;
import br.com.abce.sai.persistence.model.Construtor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class ConstrutorAssembler implements RepresentationModelAssembler<Construtor, EntityModel<ConstrutorDto>> {

    private ModelMapper modelMapper;

    public ConstrutorAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<ConstrutorDto> toModel(Construtor entity) {
        ConstrutorDto construtorDto = modelMapper.map(entity, ConstrutorDto.class);
        return EntityModel.of(construtorDto,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConstrutorController.class).findByOne(entity.getIdConstrutor())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConstrutorController.class).findAll(entity.getCnpj(), entity.getUsuarioByUsuarioIdUsuario() != null ? entity.getUsuarioByUsuarioIdUsuario().getIdUsuario() : null)).withRel("construtores"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class)
                        .findByOne((construtorDto.getUsuarioId()))).withRel("usuario"));
    }
}
