package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imovel_has_caracteristica_imovel", schema = "sai", catalog = "")
public class ImovelHasCaracteristicaImovel {

    @EmbeddedId
    private ImovelHasCaracteristicaImovelPK id;

    @Column(name = "valor", nullable = true, length = 45)
    private String valor;

    @MapsId("imovelIdImovel")
    @ManyToOne
    @JoinColumn(name = "imovel_id_imovel", referencedColumnName = "id_imovel", nullable = false)
    @JsonIgnore
    private Imovel imovelByImovelIdImovel;

    @MapsId("caracteristicaImovelIdCaracteristicaImovel")
    @ManyToOne
    @JoinColumn(name = "caracteristica_imovel_id_caracteristica_imovel", referencedColumnName = "id_caracteristica_imovel", nullable = false)
    private CaracteristicaImovel caracteristicaImovelByCaracteristicaImovelIdCaracteristicaImovel;
}
