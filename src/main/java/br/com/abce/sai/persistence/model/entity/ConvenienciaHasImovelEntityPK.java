package br.com.abce.sai.persistence.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ConvenienciaHasImovelEntityPK implements Serializable {

    @Column(name = "conveniencia_id_conveniencia", nullable = false)
    private int convenienciaIdConveniencia;

    @Column(name = "imovel_id_imovel", nullable = false)
    private int imovelIdImovel;
}
