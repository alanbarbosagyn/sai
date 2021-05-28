package br.com.abce.sai.persistence.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ImovelHasCaracteristicaImovelEntityPK implements Serializable {

    @Column(name = "imovel_id_imovel", nullable = false)
    private Long imovelIdImovel;

    @Column(name = "caracteristica_imovel_id_caracteristica_imovel", nullable = false)
    private Long caracteristicaImovelIdCaracteristicaImovel;

}
