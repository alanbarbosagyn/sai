package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "corretor_imovel_favorito", schema = "sai", catalog = "")
public class CorretorImovelFavorito {

    @EmbeddedId
    private CorretorImovelFavoritoPK id;

    @MapsId("imovelId")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "imovel_id", referencedColumnName = "id_imovel", nullable = false)
    @JsonIgnore
    private Imovel imovelByImovelId;

    @MapsId("corretorId")
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "corretor_id", referencedColumnName = "id_corretor", nullable = false)
    @JsonIgnore
    private Corretor corretorByCorretorId;
}
