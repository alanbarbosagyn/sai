package br.com.abce.sai.persistence.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ImovelHasFotoEntityPK implements Serializable {

    @Column(name = "imovel_id_imovel", nullable = false)
    private int imovelIdImovel;

    @Column(name = "foto_id_foto", nullable = false)
    private int fotoIdFoto;
}
