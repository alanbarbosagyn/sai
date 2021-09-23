package br.com.abce.sai.controller;

import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.*;
import br.com.abce.sai.persistence.repo.CorretorImovelFavoritoRepository;
import br.com.abce.sai.persistence.repo.CorretorRepository;
import br.com.abce.sai.persistence.repo.ImovelRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://mvzrxz.hospedagemelastica.com.br", "https://feedimoveis.com.br"})
@RequestMapping("/api/imovel-favorito")
@Api
public class CorretorImovelFavoritoController {

    private CorretorImovelFavoritoRepository corretorImovelFavoritoRepository;

    private ImovelRepository imovelRepository;

    private CorretorRepository corretorRepository;

    public CorretorImovelFavoritoController(CorretorImovelFavoritoRepository corretorImovelFavoritoRepository, ImovelRepository imovelRepository, CorretorRepository corretorRepository) {
        this.corretorImovelFavoritoRepository = corretorImovelFavoritoRepository;
        this.imovelRepository = imovelRepository;
        this.corretorRepository = corretorRepository;
    }


    @ApiOperation(value = "Salva imóvel favorito corretor.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<CorretorImovelFavorito>> createImovelFavorito(@RequestBody @Valid CorretorImovelFavorito corretorImovelFavorito) {

        if (corretorImovelFavorito == null || corretorImovelFavorito.getId() == null)
            throw new DataValidationException("Id do corretor e do imóvel são obrigatórios");

        Imovel imovel = imovelRepository.findById(corretorImovelFavorito.getId().getImovelId())
                .orElseThrow(() -> new DataValidationException("Imóvel não encontrado."));

        Corretor corretor = corretorRepository.findById(corretorImovelFavorito.getId().getCorretorId())
                .orElseThrow(() -> new DataValidationException("Corretor não encontrada."));

        corretorImovelFavorito.setCorretorByCorretorId(corretor);
        corretorImovelFavorito.setImovelByImovelId(imovel);

        EntityModel<CorretorImovelFavorito> corretorImovelFavoritoModel = EntityModel.of(corretorImovelFavoritoRepository.save(corretorImovelFavorito)
                ,linkTo(methodOn(CorretorImovelFavoritoController.class).findAll(corretorImovelFavorito.getId().getImovelId(), corretorImovelFavorito.getId().getCorretorId())).withSelfRel());

        return ResponseEntity.created(corretorImovelFavoritoModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(corretorImovelFavoritoModel);
    }

    @ApiOperation(value = "Deleta imóvel favorito.")
    @DeleteMapping("/{id-imovel}/{id-corretor}")
    public HttpEntity<Object> delete(@PathVariable(name = "id-imovel") @NotNull(message = "Id do imóvel favorito é obrigatório.") Long idImovel,
                                     @PathVariable(name = "id-corretor") @NotNull(message = "Id do corretor é obrigatório.") Long idCorretor) {

        CorretorImovelFavorito corretorImovelFavorito = corretorImovelFavoritoRepository.findById_ImovelIdAndId_CorretorId(idImovel, idCorretor)
                .orElseThrow(() -> new RecursoNotFoundException(CorretorImovelFavorito.class, new String("Id Imóvel " + idImovel + " Id Corretor " + idCorretor)));

        corretorImovelFavoritoRepository.delete(corretorImovelFavorito);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Lista os imóveis favoritos.")
    @GetMapping
    public Collection<EntityModel<CorretorImovelFavorito>> findAll(@RequestParam(name = "id-imovel", required = false) Long idImovel,
                                                                    @RequestParam(name = "id-corretor", required = false) Long idCorretor) {

        Collection<EntityModel<CorretorImovelFavorito>> imovelFavoritoCollection = new ArrayList<>();

        Iterable<CorretorImovelFavorito> imovelFavoritoList = idImovel != null || idCorretor != null ?
                corretorImovelFavoritoRepository.findById_ImovelIdOrId_CorretorId(idImovel, idCorretor) :
                corretorImovelFavoritoRepository.findAll();

        for (CorretorImovelFavorito corretorImovelFavorito : imovelFavoritoList) {
            EntityModel<CorretorImovelFavorito> imoveis = EntityModel.of(corretorImovelFavorito,
                    linkTo(methodOn(CorretorImovelFavoritoController.class)
                            .findAll(corretorImovelFavorito.getId().getImovelId(), corretorImovelFavorito.getId().getCorretorId())).withRel("imovel-favorito"));
            imovelFavoritoCollection.add(imoveis);
        }

        return imovelFavoritoCollection;
    }
}
