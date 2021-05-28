package br.com.abce.sai.persistence.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conveniencia", schema = "sai", catalog = "")
public class ConvenienciaEntity {

    @Id
    @Column(name = "id_conveniencia", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idConveniencia;

    @Column(name = "descricao", nullable = false, length = 45)
    private String descricao;

    @OneToMany(mappedBy = "convenienciaByConvenienciaIdConveniencia")
    private Collection<ConvenienciaHasImovelEntity> convenienciaHasImovelsByIdConveniencia;
}
