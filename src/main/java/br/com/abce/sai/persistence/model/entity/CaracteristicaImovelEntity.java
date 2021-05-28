package br.com.abce.sai.persistence.model.entity;

import br.com.abce.sai.persistence.model.TipoImovel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "caracteristica_imovel", schema = "sai", catalog = "")
public class CaracteristicaImovelEntity {


    @Id
    @Column(name = "id_caracteristica_imovel", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idCaracteristicaImovel;

    @Column(name = "descricao", nullable = true, length = 45)
    private String descricao;

    @Column(name = "tipo", nullable = true, length = 45)
    private String tipo;

    @Column(name = "icone", nullable = true, length = 45)
    private String icone;

    @Column(name = "sequencia", nullable = true)
    private Integer sequencia;

    @Column(name = "principal", nullable = true)
    private Byte principal;

//    @Column(name = "tipo_imovel_id_tipo_imovel", nullable = false)
//    private int tipoImovelIdTipoImovel;

    @ManyToOne
    @JoinColumn(name = "tipo_imovel_id_tipo_imovel", referencedColumnName = "id_tipo_imovel", nullable = false)
    private TipoImovel tipoImovelByTipoImovelIdTipoImovel;

    @OneToMany(mappedBy = "caracteristicaImovelByCaracteristicaImovelIdCaracteristicaImovel1")
    private Collection<CaracteristicaImovelListaEntity> caracteristicaImovelListasByIdCaracteristicaImovel;

    @OneToMany(mappedBy = "caracteristicaImovelByCaracteristicaImovelIdCaracteristicaImovel")
    private Collection<ImovelHasCaracteristicaImovelEntity> imovelHasCaracteristicaImovelsByIdCaracteristicaImovel;

}
