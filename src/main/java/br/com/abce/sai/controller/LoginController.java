package br.com.abce.sai.controller;


import br.com.abce.sai.dto.RequestChangePass;
import br.com.abce.sai.dto.RequestLoginDto;
import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.persistence.model.Const;
import br.com.abce.sai.persistence.model.Usuario;
import br.com.abce.sai.persistence.repo.UsuarioRepository;
import br.com.abce.sai.representacao.UsuarioAssembler;
import br.com.abce.sai.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/auth")
@Api
@CrossOrigin(origins = {"http://localhost:4200", "https://csnnft.hospedagemelastica.com.br", "https://getimoveisgo.com.br"})
public class LoginController {

    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private UsuarioAssembler usuarioAssembler;

    private MailService mailService;

    public LoginController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioAssembler usuarioAssembler, MailService mailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioAssembler = usuarioAssembler;
        this.mailService = mailService;
    }

    @PostMapping("/login")
    @ApiOperation("Login do usuário")
    public EntityModel<Usuario> login(@Valid RequestLoginDto requestLoginDto) {

        Usuario usuario = usuarioRepository.findByLogin(requestLoginDto.getLogin())
                .orElseThrow(() -> new DataValidationException("Login não encontrado."));

        if (!usuario.getSenha().equals(passwordEncoder.encode(requestLoginDto.getPassword())))
            throw new DataValidationException("Senha incorreta");

        return  usuarioAssembler.toModel(usuario);
    }

    @PostMapping("/esqueci-senha")
    @ApiOperation("Solicitar senha provisório para o usuário")
    public void esqueciSenha(@Email(message = "Login inválido.") @NotNull(message = "Login obrigatório.") String login) {

        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new DataValidationException("Login não encontrado."));

        usuario.setReperacaoSenha(Const.EM_PROCESSO_RECUPERACAO_SENHA);

        usuarioRepository.save(usuario);

        mailService.send(usuario.getLogin(), Const.ASSUNTO_SENHA_PROVISORIA, Const.CORPO_EMAIL_SENHA_PROVISORIA);
    }

    @PostMapping("/trocar-senha")
    @ApiOperation("Trocar senha do usuário")
    public void trocarSenha(@Valid RequestChangePass requestChangePass) {

        Usuario usuario = usuarioRepository.findByLogin(requestChangePass.getLogin())
                .orElseThrow(() -> new DataValidationException("Login não encontrado."));

        if (!usuario.getSenha().equals(passwordEncoder.encode(requestChangePass.getPassword())))
            throw new DataValidationException("Senha atual incorreta");

        String novaSenhaEncodada = passwordEncoder.encode(requestChangePass.getNewPassword());

        if (!usuario.getSenha().equals(novaSenhaEncodada))
            throw new DataValidationException("Senha igual a atual");

        usuario.setSenha(novaSenhaEncodada);
        usuario.setReperacaoSenha(Const.SEM_RECUPERACAO_SENHA);

        usuarioRepository.save(usuario);

        mailService.send(usuario.getLogin(), Const.ASSUNTO_TROCA_SENHA, Const.CORPO_EMAIL_TROCA_SENHA);
    }
}
