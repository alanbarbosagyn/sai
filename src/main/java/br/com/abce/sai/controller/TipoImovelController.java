package br.com.abce.sai.controller;


import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.TipoImovel;
import br.com.abce.sai.persistence.repo.TipoImoveRepository;
import br.com.abce.sai.representacao.TipoImovelAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tipo-imovel")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://mvzrxz.hospedagemelastica.com.br", "https://getimoveisgo.com.br", "https://feedimoveis.com.br"})
public class TipoImovelController {

    private final TipoImoveRepository tipoImoveRepository;

    private final TipoImovelAssembler assembler;


    public TipoImovelController(TipoImoveRepository tipoImoveRepository, TipoImovelAssembler assembler) {
        this.tipoImoveRepository = tipoImoveRepository;
        this.assembler = assembler;
    }

    @ApiOperation(value = "Consulta todos os tipos de imóveis.")
    @GetMapping
    public CollectionModel<EntityModel<TipoImovel>> findAll() {

        List<EntityModel<TipoImovel>> tipoImoveis = ((List<TipoImovel>) tipoImoveRepository.findAll())
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(tipoImoveis);
    }

    @ApiOperation(value = "Cadastra um novo tipo de imóvel.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<TipoImovel>> create(@RequestBody @Valid TipoImovel tipoImovel) {

        validaDescricaoPerfilCadastrado(tipoImovel);

        EntityModel<TipoImovel> tipoImovelEntityModel = assembler.toModel(tipoImoveRepository.save(tipoImovel));

        return ResponseEntity.created(tipoImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(tipoImovelEntityModel);
    }

    @ApiOperation(value = "Atualiza dados do imóvel.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<TipoImovel>> updateImovel(@RequestBody @Valid TipoImovel newTipoImovel, @PathVariable Long id) {

//        if (newTipoImovel.getIdTipoImovel() != null && newTipoImovel.getIdTipoImovel().equals(id)) {
//            throw new ResourcedMismatchException(id);
//        }

        validaDescricaoPerfilCadastrado(newTipoImovel);

        TipoImovel tipoImovelUpdated = tipoImoveRepository.findById(id)
                .map(tipoImovel -> {
                    tipoImovel.setDescricao(newTipoImovel.getDescricao());
                    return tipoImoveRepository.save(tipoImovel);
                })
                .orElseGet(() -> {
                    newTipoImovel.setIdTipoImovel(id);
                    return tipoImoveRepository.save(newTipoImovel);
                });

        EntityModel<TipoImovel> tipoImovelEntityModel = assembler.toModel(tipoImovelUpdated);

        return ResponseEntity
                .created(tipoImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(tipoImovelEntityModel);
    }

    @ApiOperation(value = "Consulta tipo de imóvel por ID.")
    @GetMapping("{id}")
    public EntityModel<TipoImovel> findByOne(@PathVariable @NotNull(message = "Id do tipo do imóvel obrigatório.") Long id) {

        TipoImovel employee = tipoImoveRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(TipoImovel.class, id));

        return assembler.toModel(employee);
    }

    private void validaDescricaoPerfilCadastrado(@RequestBody @Validated TipoImovel tipoImovel) {

        if (tipoImoveRepository.findByDescricao(tipoImovel.getDescricao()) != null)
            throw new DataValidationException("Tipo de imóvel já cadastrado.");
    }
}
