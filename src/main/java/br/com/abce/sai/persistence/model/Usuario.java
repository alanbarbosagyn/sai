package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario", schema = "sai", catalog = "")
public class Usuario {

    @Id
    @Column(name = "id_usuario", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "Login do usuário é obrigatório")
    @Size(message = "O login pode ter até 40 caracteres", max = 45)
    @Column(name = "login", nullable = false, length = 45)
    private String login;

    @Email(message = "E-mail inválido.")
    @NotNull(message = "E-mail do usuário é obrigatório")
    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Transient
    @Size(message = "A senha deve conter no mínimo 6 e no máximo 10 caracteres", min = 6, max = 10)
    private String senhaLimpa;

    @Transient
    private String senhaLimpaConfirmacao;

    @JsonIgnore
    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "status", nullable = false, length = 1)
    private String status;

    @OneToOne
    @JoinColumn(name = "foto_id_foto", referencedColumnName = "id_foto")
    private Foto fotoByFotoIdFoto;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthProvider authProvider;

    @Column(name = "tipo", length = 1, nullable = false)
    @NotNull(message = "O tipo de usuário é obrigatório")
    @Pattern(regexp = "[1-3]]", message = "O tipo deve ser 1 - Administrador, 2 - Corretor, 3 - Construtor")
    private String tipo;

    @Column(name = "recuperacao_senha", length = 1, nullable = false)
    private String reperacaoSenha;

    @Column(name = "confirmacao_email", length = 1, nullable = false)
    private String confirmacaoEmail;

    @Column(name = "data_cadastro", nullable = false)
    private Date dataCadastro;

    @Column(name = "data_atualizacao")
    private Date dataAtualizacao;


}
