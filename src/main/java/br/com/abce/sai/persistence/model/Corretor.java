package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "corretor", schema = "sai", catalog = "")
public class Corretor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_corretor", nullable = false)
    private Long idCorretor;

    @NotNull(message = "O número do CRECI é obrigatório.")
    @Size(message = "O número do CRECI pode conter até 20 dígitos.", max = 20)
    @Column(name = "num_creci", nullable = false, unique = true, length = 20)
    private String numCreci;

    @NotNull(message = "O nome é obrigatório.")
    @Size(message = "O nome pode conter até 45 dígitos.", max = 45)
    @Column(name = "nome", nullable = false, length = 45)
    private String nome;

    @NotNull(message = "A data de nascimento é obrigatório.")
    @Column(name = "data_nascimento", nullable = false, length = 45)
    private String dataNascimento;

    @CPF(message = "O CPF não é válido.")
    @NotNull(message = "O CPF é obrigatório.")
    @Column(name = "cpf", nullable = false, unique = true, length = 45)
    private String cpf;

    @Column(name = "telefone", nullable = true, length = 45)
    private String telefone;

    @NotNull(message = "O número de celular é obrigatório.")
    @Column(name = "celular", nullable = false, length = 45)
    private String celular;

    @Column(name = "data_cadastro", nullable = false)
    private Date dataCadastro;

    @OneToOne
    @JoinColumn(name = "usuario_id_usuario", referencedColumnName = "id_usuario", nullable = false, updatable = false)
    @JsonBackReference
    private Usuario usuarioByUsuarioIdUsuario;

    @NotNull(message = "endereço é obrigatório")
    @Valid
    @ManyToOne
    @JoinColumn(name = "endereco_id_endereco", referencedColumnName = "id_endereco", nullable = false)
    private Endereco enderecoByEnderecoIdEndereco;

    @OneToMany(mappedBy = "corretorByCorretorId")
    private Collection<CorretorImovelFavorito> imovelHasFavorito;
}
