package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.ImovelController;
import br.com.abce.sai.dto.ImovelDto;
import br.com.abce.sai.persistence.model.Imovel;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ImovelModelAssembler implements RepresentationModelAssembler<Imovel, EntityModel<ImovelDto>> {

    private ModelMapper modelMapper;

    public ImovelModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<ImovelDto> toModel(Imovel entity) {
        return EntityModel.of(modelMapper.map(entity, ImovelDto.class),
                linkTo(methodOn(ImovelController.class).findByOne(entity.getIdImovel())).withSelfRel());
    }
}
