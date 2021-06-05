package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.ConstrutorController;
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
        return EntityModel.of(modelMapper.map(entity, ConstrutorDto.class),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConstrutorController.class).findByOne(entity.getIdConstrutor())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ConstrutorController.class).findAll(entity.getCnpj())).withRel("construtores"));
    }
}
