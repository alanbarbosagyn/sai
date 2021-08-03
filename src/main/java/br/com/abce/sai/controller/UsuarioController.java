package br.com.abce.sai.controller;

import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.model.Const;
import br.com.abce.sai.persistence.model.Usuario;
import br.com.abce.sai.persistence.repo.FotoRepository;
import br.com.abce.sai.persistence.repo.UsuarioRepository;
import br.com.abce.sai.representacao.UsuarioAssembler;
import br.com.abce.sai.service.MailService;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/usuario")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://csnnft.hospedagemelastica.com.br", "https://getimoveisgo.com.br", "https://feedimoveis.com.br"})
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioAssembler assembler;

    private final FotoRepository fotoRepository;

    private final PasswordEncoder passwordEncoder;

    private MailService mailService;


    public UsuarioController(UsuarioRepository tipoImoveRepository, UsuarioAssembler assembler, FotoRepository fotoRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.usuarioRepository = tipoImoveRepository;
        this.assembler = assembler;
        this.fotoRepository = fotoRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @ApiOperation(value = "Consulta todos os usuarios.")
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> findAll(@RequestParam(name = "login", required = false) final String login,
                                                        @RequestParam(name = "email", required = false)  @Email(message = "E-mail inválido.") final String email) {

        CollectionModel collectionModel;

        if (StringUtils.isNotBlank(login) || StringUtils.isNotBlank(email)) {
            String id = (login == null ? "" : login) + (email == null ? "" : email);
            Usuario usuario = (StringUtils.isNotBlank(login)  ? usuarioRepository.findByLogin(login) :  usuarioRepository.findByEmail(email))
                    .orElseThrow(() -> new RecursoNotFoundException(Usuario.class, id));

            collectionModel = CollectionModel.of(Stream.of(usuario).map(assembler::toModel).collect(Collectors.toList()));
        } else {

            collectionModel = CollectionModel.of(((List<Usuario>) usuarioRepository.findAll())
                    .stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList()));
        }

        return collectionModel;
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

        if (StringUtils.isNotBlank(usuario.getSenhaLimpa()))
            usuario.setSenha(passwordEncoder.encode(usuario.getSenhaLimpa()));
//        usuario.setStatus(Const.USUARIO_STATUS_NAO_AUTORIZADO);
//        usuario.setConfirmacaoEmail(Const.USUARIO_EMAIL_NAO_VERIFICADO);
        usuario.setDataCadastro(new Date());

        EntityModel<Usuario> tipoImovelEntityModel = assembler.toModel(usuarioRepository.save(usuario));

        mailService.send(usuario.getLogin(), Const.ASSUNTO_EMAIL_NOVO_USUARIO, Const.CORPO_EMAIL_NOVO_USUARIO);

        return ResponseEntity.created(tipoImovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(tipoImovelEntityModel);
    }

    @ApiOperation(value = "Atualiza dados do usuário.")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> update(@RequestBody @Valid Usuario newUsuario, @PathVariable Long id) {

//        if (newUsuario.getIdUsuario() != null && newUsuario.getIdUsuario().equals(id)) {
//            throw new ResourcedMismatchException(id);
//        }

        validaSenhaUsuario(newUsuario);

        Usuario tipoImovelUpdated = usuarioRepository.findById(id)
                .map(usuario -> {

                    if (newUsuario.getFotoByFotoIdFoto() != null)
                        fotoRepository.findById(newUsuario.getFotoByFotoIdFoto().getIdFoto())
                                .ifPresent(foto -> usuario.setFotoByFotoIdFoto(foto));

                    usuario.setStatus(newUsuario.getStatus());
                    usuario.setLogin(newUsuario.getLogin());
                    usuario.setReperacaoSenha(newUsuario.getReperacaoSenha());
                    usuario.setAuthProvider(newUsuario.getAuthProvider());
                    usuario.setConfirmacaoEmail(newUsuario.getConfirmacaoEmail());
                    usuario.setTipo(newUsuario.getTipo());
                    usuario.setDataAtualizacao(new Date());
                    usuario.setEmail(newUsuario.getEmail());
//                    if (StringUtils.isNotBlank(newUsuario.getSenha()))
//                        usuario.setSenha(newUsuario.getSenha());

                    if (newUsuario.getSenhaLimpa() != null) {
                        validaSenhaUsuario(newUsuario);
                        usuario.setSenha(passwordEncoder.encode(newUsuario.getSenhaLimpa()));
                    }

                    return usuarioRepository.save(usuario);
                })
                .orElseGet(() -> {
                    newUsuario.setIdUsuario(id);

                    if (newUsuario.getSenhaLimpa() != null) {
                        validaSenhaUsuario(newUsuario);
                        newUsuario.setSenha(passwordEncoder.encode(newUsuario.getSenhaLimpa()));
                    }
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

        if (usuarioRepository.existsByEmail(usuario.getEmail()))
            throw new DataValidationException("E-mail já cadastrado.");
    }
    private void validaSenhaUsuario(@RequestBody @Validated Usuario usuario) {

        if (usuario.getSenhaLimpa() != null
                && !usuario.getSenhaLimpa().equals(usuario.getSenhaLimpaConfirmacao()))
            throw new DataValidationException("Senha divergente da confirmação.");
    }
}
