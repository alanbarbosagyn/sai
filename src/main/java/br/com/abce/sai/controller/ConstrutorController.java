package br.com.abce.sai.controller;


import br.com.abce.sai.dto.ConstrutorDto;
import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Construtor;
import br.com.abce.sai.persistence.model.Usuario;
import br.com.abce.sai.persistence.repo.ConstrutorRepository;
import br.com.abce.sai.persistence.repo.UsuarioRepository;
import br.com.abce.sai.representacao.ConstrutorAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.br.CNPJ;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/construtor")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://csnnft.hospedagemelastica.com.br"})
public class ConstrutorController {

    private final ConstrutorRepository construtorRepository;

    private final UsuarioRepository usuarioRepository;

    private final ConstrutorAssembler assembler;

    private final ModelMapper modelMapper;

    public ConstrutorController(ConstrutorRepository construtorRepository, UsuarioRepository usuarioRepository, ConstrutorAssembler assembler, ModelMapper modelMapper) {
        this.construtorRepository = construtorRepository;
        this.usuarioRepository = usuarioRepository;
        this.assembler = assembler;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Consulta todos os construtores de imóveis.")
    @GetMapping
    public CollectionModel<EntityModel<ConstrutorDto>> findAll(@RequestParam(name = "cnpj", required = false) @ApiParam(name = "CNPJ do construtor(a)") @CNPJ(message = "O CNPJ é inválido.") final String cnpj,
                                                               @RequestParam(name = "usuario-id", required = false) final Long usuarioId) {

        CollectionModel collectionModel = null;

        if (cnpj != null || (usuarioId != null && usuarioId > 0L)) {

            collectionModel = (CollectionModel) CollectionModel.of(construtorRepository.findByCnpjorUAndUsuarioByUsuarioIdUsuario_IdUsuario(cnpj, usuarioId)
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
    public EntityModel<ConstrutorDto> findByOne(@PathVariable @NotNull(message = "Id do contrutor obrigatório.") Long id) {

        Construtor construtor = construtorRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Construtor.class, id));

        return assembler.toModel(construtor);
    }

    @ApiOperation(value = "Cadastra um construtor de imóvel.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<ConstrutorDto>> create(@Valid @RequestBody ConstrutorDto construtor) {

        validaConstrutorCadastrado(construtor);

        Optional<Usuario> usuario = usuarioRepository.findByIdUsuario(construtor.getUsuarioId());

        Construtor entity = modelMapper.map(construtor, Construtor.class);

        entity.setUsuarioByUsuarioIdUsuario(usuario.orElseThrow(() -> new DataValidationException("Usuário não cadastrado.")));
        entity.setDataCadastro(new Date());

        EntityModel<ConstrutorDto> ConstrutorEntityModel = assembler.toModel(construtorRepository.save(entity));

        return ResponseEntity.created(ConstrutorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(ConstrutorEntityModel);
    }

    @ApiOperation(value = "Atualiza dados do construtor do imóvel.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ConstrutorDto>> updateImovel(@RequestBody @Valid ConstrutorDto newConstrutor, @PathVariable Long id) {

//        if (newConstrutor.getId() != null && newConstrutor.getId().equals(id)) {
//            throw new ResourcedMismatchException(id);
//        }

        Construtor construtorUpdaded = construtorRepository.findById(id)
                .map(construtor -> {
                    construtor.setCnpj(newConstrutor.getCnpj());
                    construtor.setNome(newConstrutor.getNome());
                    construtor.setUsuarioByUsuarioIdUsuario(usuarioRepository
                            .findByIdUsuario(newConstrutor.getUsuarioId())
                            .orElseThrow(() -> new DataValidationException("Usuário não cadastrado.")));

                    return construtorRepository.save(construtor);
                })
                .orElseGet(() -> {
                    Construtor construtor = modelMapper.map(newConstrutor, Construtor.class);
                    construtor.setIdConstrutor(id);
                    return construtorRepository.save(construtor);
                });

        EntityModel<ConstrutorDto> ConstrutorEntityModel = assembler.toModel(construtorUpdaded);

        return ResponseEntity
                .created(ConstrutorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(ConstrutorEntityModel);
    }

    private void validaConstrutorCadastrado(@RequestBody @Validated ConstrutorDto construtor) {

        if (construtorRepository.existsByCnpj(construtor.getCnpj()))
            throw new DataValidationException("Construtor com CNPJ já cadastrado.");
    }
}
