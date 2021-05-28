package br.com.abce.sai.persistence.model.entity;

import br.com.abce.sai.persistence.model.Imovel;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imovel_has_caracteristica_imovel", schema = "sai", catalog = "")
//@IdClass(ImovelHasCaracteristicaImovelEntityPK.class)
public class ImovelHasCaracteristicaImovelEntity {

//    @Id
//    @Column(name = "imovel_id_imovel", nullable = false)
//    private int imovelIdImovel;
//
//    @Id
//    @Column(name = "caracteristica_imovel_id_caracteristica_imovel", nullable = false)
//    private int caracteristicaImovelIdCaracteristicaImovel;

    @EmbeddedId
    private ImovelHasCaracteristicaImovelEntityPK id;

    @Column(name = "valor", nullable = true, length = 45)
    private String valor;

    @MapsId("imovelIdImovel")
    @ManyToOne
    @JoinColumn(name = "imovel_id_imovel", referencedColumnName = "id_imovel", nullable = false)
    private Imovel imovelByImovelIdImovel;

    @MapsId("caracteristicaImovelIdCaracteristicaImovel")
    @ManyToOne
    @JoinColumn(name = "caracteristica_imovel_id_caracteristica_imovel", referencedColumnName = "id_caracteristica_imovel", nullable = false)
    private CaracteristicaImovelEntity caracteristicaImovelByCaracteristicaImovelIdCaracteristicaImovel;
}
