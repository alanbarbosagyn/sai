package br.com.abce.sai.persistence.model.entity;

import br.com.abce.sai.persistence.model.Imovel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conveniencia_has_imovel", schema = "sai", catalog = "")
//@IdClass(ConvenienciaHasImovelEntityPK.class)
public class ConvenienciaHasImovelEntity {


    @EmbeddedId
    private ConvenienciaHasImovelEntityPK id;

//    @Id
//    @Column(name = "conveniencia_id_conveniencia", nullable = false)
//    private int convenienciaIdConveniencia;
//
//    @Id
//    @Column(name = "imovel_id_imovel", nullable = false)
//    private int imovelIdImovel;

    @MapsId("convenienciaIdConveniencia")
    @ManyToOne
    @JoinColumn(name = "conveniencia_id_conveniencia", referencedColumnName = "id_conveniencia", nullable = false)
    private ConvenienciaEntity convenienciaByConvenienciaIdConveniencia;

    @MapsId("imovelIdImovel")
    @ManyToOne
    @JoinColumn(name = "imovel_id_imovel", referencedColumnName = "id_imovel", nullable = false)
    private Imovel imovelByImovelIdImovel;
}
