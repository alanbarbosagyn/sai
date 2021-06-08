package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
}
