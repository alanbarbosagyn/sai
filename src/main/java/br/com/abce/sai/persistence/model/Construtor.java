package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "construtor", schema = "sai", catalog = "")
public class Construtor {

    @Id
    @Column(name = "id_construtor", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idConstrutor;

    @NotNull(message = "O nome é obrigatório.")
    @Size(message = "O nome pode conter até 45 caracteres.", max = 45)
    @Column(name = "nome", nullable = true, length = 45)
    private String nome;

    @CNPJ
    @Size(message = "O CNPJ deve ter 14 dígitos.", max = 14)
    @Column(name = "cnpj", nullable = true, length = 14)
    private String cnpj;

//    @Column(name = "usuario_id_usuario", nullable = false)
//    private Long usuarioIdUsuario;

    @Column(name = "data_cadastro", nullable = true)
    private Date dataCadastro;

    @OneToOne
    @JoinColumn(name = "usuario_id_usuario", referencedColumnName = "id_usuario", nullable = false, updatable = false)
    private Usuario usuarioByUsuarioIdUsuario;

    @OneToMany(mappedBy = "construtorByConstrutorIdConstrutor")
    private Collection<Imovel> imovelsByIdConstrutor;

}
