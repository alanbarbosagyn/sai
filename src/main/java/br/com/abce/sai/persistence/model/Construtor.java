package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "nome", nullable = true, length = 45)
    private String nome;

    @Column(name = "cnpj", nullable = true, length = 14)
    private String cnpj;

//    @Column(name = "usuario_id_usuario", nullable = false)
//    private Long usuarioIdUsuario;

    @Column(name = "data_cadastro", nullable = true)
    private Date dataCadastro;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_usuario", referencedColumnName = "id_usuario", nullable = false, updatable = false, insertable = false)
    @JsonBackReference
    @JsonIgnoreProperties
    private Usuario usuarioByUsuarioIdUsuario;

    @OneToMany(mappedBy = "construtorByConstrutorIdConstrutor", fetch = FetchType.LAZY)
    private Collection<Imovel> imovelsByIdConstrutor;

}
