package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conveniencia", schema = "sai", catalog = "")
public class Conveniencia {

    @Id
    @Column(name = "id_conveniencia", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idConveniencia;

    @Column(name = "descricao", nullable = false, length = 45)
    private String descricao;

//    @OneToMany(mappedBy = "convenienciaByConvenienciaIdConveniencia")
//    private Collection<ConvenienciaHasImovel> convenienciaHasImovelsByIdConveniencia;
}
