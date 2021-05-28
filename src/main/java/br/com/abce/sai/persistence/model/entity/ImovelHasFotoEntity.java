package br.com.abce.sai.persistence.model.entity;

import br.com.abce.sai.persistence.model.Imovel;
import lombok.*;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imovel_has_foto", schema = "sai", catalog = "")
//@IdClass(ImovelHasFotoEntityPK.class)
public class ImovelHasFotoEntity {

    @EmbeddedId
    private ImovelHasFotoEntityPK id;

    @Column(name = "imovel_has_fotocol", nullable = true, length = 45)
    private String imovelHasFotocol;

    @MapsId("imovelIdImovel")
    @ManyToOne
    @JoinColumn(name = "imovel_id_imovel", referencedColumnName = "id_imovel", nullable = false)
    private Imovel imovelByImovelIdImovel;

    @MapsId("fotoIdFoto")
    @ManyToOne
    @JoinColumn(name = "foto_id_foto", referencedColumnName = "id_foto", nullable = false)
    private FotoEntity fotoByFotoIdFoto;
}
