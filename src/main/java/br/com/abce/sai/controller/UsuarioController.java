package br.com.abce.sai.controller;

import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Usuario;
import br.com.abce.sai.persistence.repo.FotoRepository;
import br.com.abce.sai.persistence.repo.UsuarioRepository;
import br.com.abce.sai.representacao.UsuarioAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuario")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://csnnft.hospedagemelastica.com.br", "https://getimoveisgo.com.br"})
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioAssembler assembler;

    private final FotoRepository fotoRepository;

    private final PasswordEncoder passwordEncoder;


    public UsuarioController(UsuarioRepository tipoImoveRepository, UsuarioAssembler assembler, FotoRepository fotoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = tipoImoveRepository;
        this.assembler = assembler;
        this.fotoRepository = fotoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation(value = "Consulta todos os usuarios.")
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> findAll() {

        List<EntityModel<Usuario>> usuarios = ((List<Usuario>) usuarioRepository.findAll())
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios);
    }

    @ApiOperation(value = "Cadastra um novo usuário.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<Usuario>> create(@RequestBody @Valid Usuario usuario) {

        validaCadastroUsuario(usuario);
        validaSenhaUsuario(usuario);

        if (usuario.getFotoByFotoIdFoto() != null && usuario.getFotoByFotoIdFoto().getIdFoto() > 0 )
            fotoRepository.findById(usuario.getFotoByFotoIdFoto().getIdFoto())
                    .ifPresent(foto -> usuario.setFotoByFotoIdFoto(foto));

        usuario.setSenha(passwordEncoder.encode(usuario.getSenhaLimpa()));

        EntityModel<Usuario> tipoImovelEntityModel = assembler.toModel(usuarioRepository.save(usuario));

        return ResponseEntity.created(tipoImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(tipoImovelEntityModel);
    }

    @ApiOperation(value = "Atualiza dados do usuário.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> update(@RequestBody @Valid Usuario newUsuario, @PathVariable Long id) {

//        if (newUsuario.getIdUsuario() != null && newUsuario.getIdUsuario().equals(id)) {
//            throw new ResourcedMismatchException(id);
//        }

        Usuario tipoImovelUpdated = usuarioRepository.findById(id)
                .map(usuario -> {

                    if (newUsuario.getFotoByFotoIdFoto() != null)
                        fotoRepository.findById(newUsuario.getFotoByFotoIdFoto().getIdFoto())
                                .ifPresent(foto -> usuario.setFotoByFotoIdFoto(foto));

                    usuario.setStatus(newUsuario.getStatus());
                    usuario.setLogin(newUsuario.getLogin());

                    if (newUsuario.getSenhaLimpa() != null) {
                        validaSenhaUsuario(newUsuario);
                        usuario.setSenha(passwordEncoder.encode(newUsuario.getSenhaLimpa()));
                    }

                    return usuarioRepository.save(usuario);
                })
                .orElseGet(() -> {
                    newUsuario.setIdUsuario(id);
                    return usuarioRepository.save(newUsuario);
                });

        EntityModel<Usuario> tipoImovelEntityModel = assembler.toModel(tipoImovelUpdated);

        return ResponseEntity
                .created(tipoImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(tipoImovelEntityModel);
    }

    @ApiOperation(value = "Consulta usuário por ID.")
    @GetMapping("{id}")
    public EntityModel<Usuario> findByOne(@PathVariable @NotNull(message = "Id do usuário é obrigatório.") Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(Usuario.class, id));

        return assembler.toModel(usuario);
    }

    private void validaCadastroUsuario(@RequestBody @Validated Usuario usuario) {

        if (usuarioRepository.existsByLogin(usuario.getLogin()))
            throw new DataValidationException("Login já cadastrado.");
    }
    private void validaSenhaUsuario(@RequestBody @Validated Usuario usuario) {

        if (usuario.getSenhaLimpa() != null
                && !usuario.getSenhaLimpa().equals(usuario.getSenhaLimpaConfirmacao()))
            throw new DataValidationException("Senha divergente da confirmação.");
    }
}
