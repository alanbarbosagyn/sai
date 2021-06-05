package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ImovelHasFotoPK implements Serializable {

    @NotNull(message = "O id do imóvel é obrigatório")
    @Column(name = "imovel_id_imovel", nullable = false)
    private Long imovelIdImovel;

    @Column(name = "foto_id_foto", nullable = false)
    private Long fotoIdFoto;
}
