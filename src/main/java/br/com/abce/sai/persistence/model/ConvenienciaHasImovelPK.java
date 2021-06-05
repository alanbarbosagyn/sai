package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ConvenienciaHasImovelPK implements Serializable {

    @Column(name = "conveniencia_id_conveniencia", nullable = false)
    private Long convenienciaIdConveniencia;

    @Column(name = "imovel_id_imovel", nullable = false)
    private Long imovelIdImovel;
}
