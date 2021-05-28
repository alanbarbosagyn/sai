package br.com.abce.sai.controller;


import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.exception.ResourcedMismatchException;
import br.com.abce.sai.persistence.model.Construtor;
import br.com.abce.sai.persistence.model.Usuario;
import br.com.abce.sai.persistence.repo.ConstrutorRepository;
import br.com.abce.sai.persistence.repo.UsuarioRepository;
import br.com.abce.sai.representacao.ConstrutorAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/construtor")
@Api
@CrossOrigin(origins = "http://localhost:4200")
public class ConstrutorController {

    private final ConstrutorRepository construtorRepository;

    private final UsuarioRepository usuarioRepository;

    private final ConstrutorAssembler assembler;

    public ConstrutorController(ConstrutorRepository construtorRepository, UsuarioRepository usuarioRepository, ConstrutorAssembler assembler) {
        this.construtorRepository = construtorRepository;
        this.usuarioRepository = usuarioRepository;
        this.assembler = assembler;
    }

    @ApiOperation(value = "Consulta todos os construtores de imóveis.")
    @GetMapping
    public CollectionModel<EntityModel<Construtor>> findAll(@RequestParam @ApiParam(name = "CNPJ do construtor(a)") @CNPJ(message = "O CNPJ é inválido.") final String cnpj) {

        CollectionModel collectionModel = null;

        if (cnpj != null) {

            collectionModel = (CollectionModel) CollectionModel.of(construtorRepository.findByCnpj(cnpj)
                    .orElseThrow(() -> new RecursoNotFoundException(Construtor.class, cnpj)));
        } else {

            collectionModel = CollectionModel.of(((List<Construtor>) construtorRepository.findAll())
                    .stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList()));
        }

        return collectionModel;
    }

    @ApiOperation(value = "Consulta um construtor de imóvel por ID.")
    @GetMapping("{id}")
    public EntityModel<Construtor> findByOne(@PathVariable @NotNull(message = "Id do contrutor obrigatório.") Long id) {

        Construtor construtor = construtorRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Construtor.class, id));

        return assembler.toModel(construtor);
    }

    @ApiOperation(value = "Cadastra um construtor de imóvel.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<Construtor>> create(@Validated @RequestBody Construtor construtor) {

        validaConstrutorCadastrado(construtor);

        Optional<Usuario> usuario = usuarioRepository.findByIdUsuario(construtor.getUsuarioByUsuarioIdUsuario().getIdUsuario());

        construtor.setUsuarioByUsuarioIdUsuario(usuario.orElseThrow(() -> new DataValidationException("Usuário não cadastrado.")));
        construtor.setDataCadastro(new Date());

        EntityModel<Construtor> ConstrutorEntityModel = assembler.toModel(construtorRepository.save(construtor));

        return ResponseEntity.created(ConstrutorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(ConstrutorEntityModel);
    }

    @ApiOperation(value = "Atualiza dados do construtor do imóvel.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Construtor>> updateImovel(@RequestBody Construtor newConstrutor, @PathVariable Long id) {

        if (newConstrutor.getIdConstrutor() != null && newConstrutor.getIdConstrutor().equals(id)) {
            throw new ResourcedMismatchException(id);
        }

        validaConstrutorCadastrado(newConstrutor);

        Construtor ConstrutorUpdaded = construtorRepository.findById(id)
                .map(construtor -> {
                    construtor.setCnpj(newConstrutor.getCnpj());
                    construtor.setNome(newConstrutor.getNome());
                    construtor.setUsuarioByUsuarioIdUsuario(usuarioRepository
                            .findByIdUsuario(construtor.getUsuarioByUsuarioIdUsuario().getIdUsuario())
                            .orElseThrow(() -> new DataValidationException("Usuário não cadastrado.")));

                    return construtorRepository.save(construtor);
                })
                .orElseGet(() -> {
                    newConstrutor.setIdConstrutor(id);
                    return construtorRepository.save(newConstrutor);
                });

        EntityModel<Construtor> ConstrutorEntityModel = assembler.toModel(ConstrutorUpdaded);

        return ResponseEntity
                .created(ConstrutorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(ConstrutorEntityModel);
    }

    private void validaConstrutorCadastrado(@RequestBody @Validated Construtor construtor) {

        if (construtorRepository.existsByCnpj(construtor.getCnpj()))
            throw new DataValidationException("Construtor com CNPJ já cadastrado.");
    }
}
