package br.com.abce.sai.controller;

import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.CaracteristicaImovel;
import br.com.abce.sai.persistence.repo.CaracteristicaImovelRepository;
import br.com.abce.sai.representacao.CaracteristicaAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/caracteristica-imovel")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://mvzrxz.hospedagemelastica.com.br", "https://getimoveisgo.com.br", "https://feedimoveis.com.br"})
public class CaracteristicaController {

    private CaracteristicaImovelRepository caracteristicaImovelRepository;

    private CaracteristicaAssembler assembler;

    public CaracteristicaController(CaracteristicaImovelRepository caracteristicaImovelRepository, CaracteristicaAssembler assembler) {
        this.caracteristicaImovelRepository = caracteristicaImovelRepository;
        this.assembler = assembler;
    }


    @ApiOperation(value = "Consulta todos as características de imóveis.")
    @GetMapping
    public CollectionModel<EntityModel<CaracteristicaImovel>> findAll(
            @RequestParam(name = "nome", required = false)
            @ApiParam(name = "nome", value = "Nome da característica")
            @Size(message = "Nome da característica com tamanho superior ao permitido.", max = 45)
            final String nome,

            @RequestParam(name = "tipo-imovel", required = false)
            @ApiParam(name = "tipoImovel", value = "Tipo imovel")
            final Long idTipoImovel) {

        Iterable<CaracteristicaImovel> caracteristicaImovels = nome != null  ?
                caracteristicaImovelRepository.findByDescricaoIsContaining(nome) : caracteristicaImovelRepository.findAll();

        List<EntityModel<CaracteristicaImovel>> caracteristicasLista = ((List<CaracteristicaImovel>) caracteristicaImovels).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(caracteristicasLista, linkTo(methodOn(CaracteristicaController.class).findAll(nome, idTipoImovel)).withSelfRel());
    }

    @ApiOperation(value = "Consulta uma conveniência de imóvel por ID.")
    @GetMapping("{id}")
    public EntityModel<CaracteristicaImovel> findByOne(@PathVariable @NotNull(message = "Id da característica obrigatório.") Long id) {

        CaracteristicaImovel caracteristica = caracteristicaImovelRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(CaracteristicaImovel.class, id));

        return assembler.toModel(caracteristica);
    }

    @ApiOperation(value = "Cadastra uma nova característica de imóvel.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<CaracteristicaImovel>> create(@Valid @RequestBody CaracteristicaImovel caracteristicaImovel) {

        EntityModel<CaracteristicaImovel> caracteristicaImovelEntityModel = assembler.toModel(caracteristicaImovelRepository.save(caracteristicaImovel));

        return ResponseEntity.created(caracteristicaImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(caracteristicaImovelEntityModel);
    }
}
