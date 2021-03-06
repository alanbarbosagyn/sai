package br.com.abce.sai.controller;

import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Corretor;
import br.com.abce.sai.persistence.model.Endereco;
import br.com.abce.sai.persistence.model.Usuario;
import br.com.abce.sai.persistence.repo.CorretorRepository;
import br.com.abce.sai.persistence.repo.EnderecoRepository;
import br.com.abce.sai.persistence.repo.UsuarioRepository;
import br.com.abce.sai.representacao.CorretorAssembler;
import br.com.abce.sai.service.MunicipioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.br.CPF;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://mvzrxz.hospedagemelastica.com.br", "https://feedimoveis.com.br"})
@RequestMapping("/api/corretor")
@Api
public class CorretorController {

    private final CorretorRepository corretorRepository;

    private final UsuarioRepository usuarioRepository;

    private final EnderecoRepository enderecoRepository;

    private final CorretorAssembler assembler;

    private final UsuarioController usuarioController;

    private MunicipioService municipioService;

    public CorretorController(CorretorRepository corretorRepository, UsuarioRepository usuarioRepository, EnderecoRepository enderecoRepository, CorretorAssembler assembler, UsuarioController usuarioController, MunicipioService municipioService) {
        this.corretorRepository = corretorRepository;
        this.usuarioRepository = usuarioRepository;
        this.enderecoRepository = enderecoRepository;
        this.assembler = assembler;
        this.usuarioController = usuarioController;
        this.municipioService = municipioService;
    }

    @ApiOperation(value = "Consulta todos os corretores de im??veis.")
    @GetMapping
    public CollectionModel<EntityModel<Corretor>> findAll(@RequestParam(name = "cpf", required = false) @CPF(message = "O CPF ?? inv??lido.") final String cpf,
                                                          @RequestParam(name = "creci", required = false) final String creci,
                                                          @RequestParam(name = "usuario-id", required = false) final Long usuarioId
    ) {

        CollectionModel<EntityModel<Corretor>> collectionModel;

        if (cpf != null || creci != null || (usuarioId != null && usuarioId > 0L)) {

            String id = (cpf == null ? "" : cpf) + (creci == null ? "" : creci) + (usuarioId == null ? "" : usuarioId) ;
            Corretor corretor = corretorRepository.findByNumCreciOrCpfOrUsuarioByUsuarioIdUsuario_IdUsuario(creci, cpf, usuarioId)
                    .orElseThrow(() -> new RecursoNotFoundException(Corretor.class, id));

            collectionModel = CollectionModel.of(Stream.of(corretor).map(assembler::toModel).collect(Collectors.toList()));

        } else {

            collectionModel = CollectionModel.of(((List<Corretor>) corretorRepository.findAll())
                    .stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList()));
        }
        return collectionModel;
    }

    @ApiOperation(value = "Consulta um corretor de im??vel por ID.")
    @GetMapping("{id}")
    public EntityModel<Corretor> findByOne(@PathVariable @NotNull(message = "Id do corretor obrigat??rio.") Long id) {

        Corretor corretor = corretorRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Corretor.class, id));

        return assembler.toModel(corretor);
    }

    @ApiOperation(value = "Cadastra um corretor de im??vel.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<Corretor>> create(@Valid @RequestBody Corretor corretor) {

        if (corretor.getUsuarioByUsuarioIdUsuario() != null) {
            ResponseEntity<EntityModel<Usuario>> usuario = usuarioController.create(corretor.getUsuarioByUsuarioIdUsuario());
            corretor.setUsuarioByUsuarioIdUsuario(Objects.requireNonNull(usuario.getBody()).getContent());
        }

        if (corretor.getUsuarioId() != null) {
                corretor.setUsuarioByUsuarioIdUsuario(usuarioRepository
                    .findByIdUsuario(corretor.getUsuarioId())
                    .orElseThrow(() -> new DataValidationException("Usu??rio n??o cadastrado.")));
        }

        validaCorretorCadastrado(corretor);

        corretor.getEnderecoByEnderecoIdEndereco().setMunicipio(municipioService.getMunicipio(corretor.getEnderecoByEnderecoIdEndereco().getMunicipio()));

        corretor.setDataCadastro(new Date());

        Endereco enderecoCorretor = enderecoRepository.save(corretor.getEnderecoByEnderecoIdEndereco());

        corretor.setEnderecoByEnderecoIdEndereco(enderecoCorretor);

        EntityModel<Corretor> CorretorEntityModel = assembler.toModel(corretorRepository.save(corretor));

        return ResponseEntity.created(CorretorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(CorretorEntityModel);
    }

    @ApiOperation(value = "Atualiza dados do perfil do im??vel.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Corretor>> updateImovel(@RequestBody @Valid Corretor newCorretor, @PathVariable Long id) {

//        if (newCorretor.getIdCorretor() != null && newCorretor.getIdCorretor().equals(id)) {
//            throw new ResourcedMismatchException(id);
//        }

//        validaCorretorCadastrado(newCorretor);

        newCorretor.getEnderecoByEnderecoIdEndereco().setMunicipio(municipioService.getMunicipio(newCorretor.getEnderecoByEnderecoIdEndereco().getMunicipio()));

        Corretor CorretorUpdaded = corretorRepository.findById(id)
                .map(corretor -> {
                    corretor.setCelular(newCorretor.getCelular());
                    corretor.setCpf(newCorretor.getCpf());
                    corretor.setDataNascimento(newCorretor.getDataNascimento());
                    corretor.setTelefone(newCorretor.getTelefone());
                    corretor.setUsuarioByUsuarioIdUsuario(usuarioRepository
                            .findByIdUsuario(corretor.getUsuarioByUsuarioIdUsuario().getIdUsuario())
                            .orElseThrow(() -> new DataValidationException("Usu??rio n??o cadastrado.")));

                    if (newCorretor.getEnderecoByEnderecoIdEndereco() != null)
                        corretor.getEnderecoByEnderecoIdEndereco().setBairro(newCorretor.getEnderecoByEnderecoIdEndereco().getBairro());
                        corretor.getEnderecoByEnderecoIdEndereco().setCep(newCorretor.getEnderecoByEnderecoIdEndereco().getCep());
                        corretor.getEnderecoByEnderecoIdEndereco().setCidade(newCorretor.getEnderecoByEnderecoIdEndereco().getCidade());
                        corretor.getEnderecoByEnderecoIdEndereco().setComplemento(newCorretor.getEnderecoByEnderecoIdEndereco().getComplemento());
                        corretor.getEnderecoByEnderecoIdEndereco().setLogradouro(newCorretor.getEnderecoByEnderecoIdEndereco().getLogradouro());
                        corretor.getEnderecoByEnderecoIdEndereco().setNumero(newCorretor.getEnderecoByEnderecoIdEndereco().getNumero());
                        corretor.getEnderecoByEnderecoIdEndereco().setUf(newCorretor.getEnderecoByEnderecoIdEndereco().getUf());
                        corretor.getEnderecoByEnderecoIdEndereco().setMunicipio(newCorretor.getEnderecoByEnderecoIdEndereco().getMunicipio());

                    return corretorRepository.save(corretor);
                })
                .orElseGet(() -> {
                    newCorretor.setIdCorretor(id);
                    return corretorRepository.save(newCorretor);
                });

        EntityModel<Corretor> CorretorEntityModel = assembler.toModel(CorretorUpdaded);

        return ResponseEntity
                .created(CorretorEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(CorretorEntityModel);
    }

    private void validaCorretorCadastrado(@RequestBody @Validated Corretor corretor) {

        if (corretorRepository.findByCpfEquals(corretor.getCpf())
                .isPresent()) throw new DataValidationException("Corretor com CPF j?? cadastrado.");

        if (corretorRepository.findByNumCreciEquals(corretor.getNumCreci())
                .isPresent())
            throw new DataValidationException("Corretor com N?? de CRECI j?? cadastrado.");
    }
}
