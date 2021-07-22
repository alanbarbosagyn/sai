package br.com.abce.sai.controller;

import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Conveniencia;
import br.com.abce.sai.persistence.repo.ConvenienciaRepositoy;
import br.com.abce.sai.representacao.ConvenienciaAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/api/conveniencia-imovel")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://csnnft.hospedagemelastica.com.br", "https://getimoveisgo.com.br"})
public class ConvenienciaController {

    private ConvenienciaRepositoy repositoy;

    private ConvenienciaAssembler assembler;

    public ConvenienciaController(ConvenienciaRepositoy repositoy, ConvenienciaAssembler assembler) {
        this.repositoy = repositoy;
        this.assembler = assembler;
    }

    @ApiOperation(value = "Consulta todos as conveniências de imóveis.")
    @GetMapping
    public CollectionModel<EntityModel<Conveniencia>> findAll(
            @RequestParam(name = "nome", required = false)
            @ApiParam(name = "nome", value = "Nome da conveniência")
            @Size(message = "Nome conveniência com tamanho superior ao permitido.", max = 45)
            final String nome) {

        Iterable<Conveniencia> conveniencias = nome != null  ?
                repositoy.findByDescricaoIsContaining(nome) : repositoy.findAll();

        List<EntityModel<Conveniencia>> convenienciasList = ((List<Conveniencia>) conveniencias).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(convenienciasList, linkTo(methodOn(ConvenienciaController.class).findAll(nome)).withSelfRel());
    }

    @ApiOperation(value = "Consulta uma conveniência de imóvel por ID.")
    @GetMapping("{id}")
    public EntityModel<Conveniencia> findByOne(@PathVariable @NotNull(message = "Id da conveniência obrigatório.") Long id) {

        Conveniencia conveniencia = repositoy.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Conveniencia.class, id));

        return assembler.toModel(conveniencia);
    }
}
