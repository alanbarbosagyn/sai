package br.com.abce.sai.controller;

import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Municipio;
import br.com.abce.sai.persistence.model.Regiao;
import br.com.abce.sai.persistence.repo.RegiaoRepository;
import br.com.abce.sai.representacao.RegiaoAssembler;
import br.com.abce.sai.service.MunicipioService;
import io.micrometer.core.instrument.util.StringUtils;
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
@CrossOrigin(origins = {"http://localhost:4200", "https://mvzrxz.hospedagemelastica.com.br", "https://feedimoveis.com.br"})
@RequestMapping("/api/regiao")
@Api
public class RegiaoController {

    private RegiaoRepository regiaoRepository;

    private MunicipioService municipioService;

    private RegiaoAssembler assembler;

    public RegiaoController(RegiaoRepository regiaoRepository, MunicipioService municipioService, RegiaoAssembler assembler) {
        this.regiaoRepository = regiaoRepository;
        this.municipioService = municipioService;
        this.assembler = assembler;
    }

    @ApiOperation(value = "Consulta todas as regiões do municipio.")
    @GetMapping
    public CollectionModel<EntityModel<Regiao>> findAll(final Long idMunicipio, final String codgIbge) {

        List<Regiao> regiaoSet;

        if ((idMunicipio != null && idMunicipio > 0L) || StringUtils.isNotBlank(codgIbge)) {
            regiaoSet = (List<Regiao>) regiaoRepository.findAllByMunicipio_CodgIbgeOrMunicipio_IdMunicipio(codgIbge, idMunicipio);
        } else {
            regiaoSet = (List<Regiao>) regiaoRepository.findAll();
        }

        List<EntityModel<Regiao>> regioes = regiaoSet
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(regioes);
    }

    @ApiOperation(value = "Consulta uma região por ID.")
    @GetMapping("{id}")
    public EntityModel<Regiao> findByOne(@PathVariable @NotNull(message = "Id do perfil do imóvel obrigatório.") Long id) {

        Regiao Regiao = regiaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Regiao.class, id));

        return assembler.toModel(Regiao);
    }

    @ApiOperation(value = "Cadastra uma nova região.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<Regiao>> create(@Validated @RequestBody Regiao regiao) {

        Municipio municipio = municipioService.getMunicipio(regiao.getMunicipio());

        regiao.setMunicipio(municipio);

        EntityModel<Regiao> RegiaoEntityModel = assembler.toModel(regiaoRepository.save(regiao));

        return ResponseEntity.created(RegiaoEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(RegiaoEntityModel);
    }

    @ApiOperation(value = "Atualiza dados da região.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Regiao>> updateRegiao(@RequestBody @Valid Regiao newRegiao, @PathVariable Long id) {

        Municipio municipio = municipioService.getMunicipio(newRegiao.getMunicipio());

        newRegiao.setMunicipio(municipio);

        Regiao RegiaoUpdaded = regiaoRepository.findById(id)
                .map(imovel -> {
                    imovel.setNomeRegiao(newRegiao.getNomeRegiao());
                    imovel.setLatitude(newRegiao.getLatitude());
                    imovel.setLongitude(newRegiao.getLongitude());
                    imovel.setMunicipio(newRegiao.getMunicipio());
                    return regiaoRepository.save(imovel);
                })
                .orElseGet(() -> {
                    newRegiao.setIdRegiao(id);
                    return regiaoRepository.save(newRegiao);
                });

        EntityModel<Regiao> RegiaoEntityModel = assembler.toModel(RegiaoUpdaded);

        return ResponseEntity
                .created(RegiaoEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(RegiaoEntityModel);
    }
}
