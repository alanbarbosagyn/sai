package br.com.abce.sai.representacao;

import br.com.abce.sai.controller.FotoController;
import br.com.abce.sai.controller.ImovelController;
import br.com.abce.sai.dto.ImovelDto;
import br.com.abce.sai.persistence.model.Conveniencia;
import br.com.abce.sai.persistence.model.ConvenienciaHasImovel;
import br.com.abce.sai.persistence.model.Imovel;
import br.com.abce.sai.persistence.model.ImovelHasFoto;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

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

        ArrayList builderFotos = new ArrayList<WebMvcLinkBuilder>();

        builderFotos.add(linkTo(methodOn(ImovelController.class).findByOne(entity.getIdImovel())).withSelfRel());

        if (entity.getImovelHasFotosByIdImovel() != null)
            for (ImovelHasFoto imovelHasFoto : entity.getImovelHasFotosByIdImovel()) {
                builderFotos.add(linkTo(methodOn(FotoController.class).findByOne(imovelHasFoto.getId().getFotoIdFoto(), null, null)).withRel("fotos"));
            }

        ImovelDto imovelDto = modelMapper.map(entity, ImovelDto.class);

        if (entity.getConvenienciaHasImovelsByIdImovel() != null) {
            Collection<Conveniencia> convenienciaCollection = new ArrayList<>();
            for (ConvenienciaHasImovel convenienciaHasImovel : entity.getConvenienciaHasImovelsByIdImovel())
                convenienciaCollection.add(convenienciaHasImovel.getConvenienciaByConvenienciaIdConveniencia());

            imovelDto.setListaConveniencia(convenienciaCollection);
        }

        return EntityModel.of(imovelDto,
                builderFotos);
    }
}
