package br.com.abce.sai.controller;

import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.PerfilImovel;
import br.com.abce.sai.persistence.repo.PerfilImovelRepository;
import br.com.abce.sai.representacao.PerfilImovelAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/perfil-imovel")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://csnnft.hospedagemelastica.com.br", "https://getimoveisgo.com.br"})
public class PerfilImovelController {

    private final PerfilImovelRepository perfilImovelRepository;

    private final PerfilImovelAssembler assembler;

    public PerfilImovelController(PerfilImovelRepository perfilImovelRepository, PerfilImovelAssembler assembler) {
        this.perfilImovelRepository = perfilImovelRepository;
        this.assembler = assembler;
    }

    @ApiOperation(value = "Consulta todos os tipos de perfis de imóveis.")
    @GetMapping
    public CollectionModel<EntityModel<PerfilImovel>> findAll() {

        List<EntityModel<PerfilImovel>> perfilImovels = ((List<PerfilImovel>) perfilImovelRepository.findAll())
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(perfilImovels);
    }

    @ApiOperation(value = "Consulta um perfil de imóvel por ID.")
    @GetMapping("{id}")
    public EntityModel<PerfilImovel> findByOne(@PathVariable @NotNull(message = "Id do perfil do imóvel obrigatório.") Long id) {

        PerfilImovel perfilImovel = perfilImovelRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(PerfilImovel.class, id));

        return assembler.toModel(perfilImovel);
    }

    @ApiOperation(value = "Cadastra um novo perfil de imóvel.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<PerfilImovel>> create(@Validated @RequestBody PerfilImovel perfilImovel) {

        validaDescricaoPerfilCadastrado(perfilImovel);

        EntityModel<PerfilImovel> perfilImovelEntityModel = assembler.toModel(perfilImovelRepository.save(perfilImovel));

        return ResponseEntity.created(perfilImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(perfilImovelEntityModel);
    }

    private void validaDescricaoPerfilCadastrado(@RequestBody @Validated PerfilImovel perfilImovel) {

        if (perfilImovelRepository.findByDescricao(perfilImovel.getDescricao()) != null)
            throw new DataValidationException("Perfil de imóvel já cadastrado.");
    }

    @ApiOperation(value = "Atualiza dados do perfil do imóvel.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PerfilImovel>> updateImovel(@RequestBody PerfilImovel newPerfilImovel, @PathVariable Long id) {

//        if (newPerfilImovel.getIdPerfilImovel() != null && newPerfilImovel.getIdPerfilImovel().equals(id)) {
//            throw new ResourcedMismatchException(id);
//        }

        validaDescricaoPerfilCadastrado(newPerfilImovel);

        PerfilImovel perfilImovelUpdaded = perfilImovelRepository.findById(id)
                .map(imovel -> {
                    imovel.setDescricao(newPerfilImovel.getDescricao());
                    return perfilImovelRepository.save(imovel);
                })
                .orElseGet(() -> {
                    newPerfilImovel.setIdPerfilImovel(id);
                    return perfilImovelRepository.save(newPerfilImovel);
                });

        EntityModel<PerfilImovel> perfilImovelEntityModel = assembler.toModel(perfilImovelUpdaded);

        return ResponseEntity
                .created(perfilImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(perfilImovelEntityModel);
    }
}
