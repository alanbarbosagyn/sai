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
public class CorretorImovelFavoritoPK implements Serializable {

    @Column(name = "imovel_id", nullable = false)
    private Long imovelId;

    @Column(name = "corretor_id", nullable = false)
    private Long corretorId;
}
