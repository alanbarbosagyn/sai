package br.com.abce.sai.controller;

import br.com.abce.sai.persistence.model.Municipio;
import br.com.abce.sai.persistence.repo.MunicipioRepository;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://mvzrxz.hospedagemelastica.com.br", "https://feedimoveis.com.br"})
@RequestMapping("/api/municipio")
@Api
public class MunicipioControler {

    private MunicipioRepository municipioRepository;

    public MunicipioControler(MunicipioRepository municipioRepository) {
        this.municipioRepository = municipioRepository;
    }

    @ApiOperation(value = "Consulta todos os municípios.")
    @GetMapping
    public CollectionModel<EntityModel<Municipio>> findAll(@RequestParam @NotNull(message = "UF do estado obrigatório.") final String ufEstado, @RequestParam(required = false) @Size(min = 3, message = "Nome do município deve ter pelo menos 3 caracteres") final String nomeMunicipio) {

        List<Municipio> municipios;

        if (StringUtils.isNotBlank(nomeMunicipio)) {
            municipios = (List<Municipio>) municipioRepository.findByUfEstadoAndNomeMunicipioIsContaining(ufEstado, nomeMunicipio);
        } else {
            municipios = (List<Municipio>) municipioRepository.findByUfEstado(ufEstado);
        }

        List<EntityModel<Municipio>> municipiosEntity = municipios
                .stream()
                .map(municipio -> EntityModel.of(municipio,
                        linkTo(methodOn(MunicipioControler.class).findAll(municipio.getUfEstado(), municipio.getNomeMunicipio())).withRel("municipio")))
                .collect(Collectors.toList());

        return CollectionModel.of(municipiosEntity);
    }
}
