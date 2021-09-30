package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imovel_has_foto", schema = "sai")
//@IdClass(ImovelHasFotoEntityPK.class)
public class ImovelHasFoto {

    @EmbeddedId
    private ImovelHasFotoPK id;

    @NotNull(message = "A ordem da foto é obrigatório")
    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean capa;

    @MapsId("imovelIdImovel")
    @ManyToOne
    @JoinColumn(name = "imovel_id_imovel", referencedColumnName = "id_imovel", nullable = false)
    @JsonIgnore
    private Imovel imovelByImovelIdImovel;

    @MapsId("fotoIdFoto")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "foto_id_foto", referencedColumnName = "id_foto", nullable = false)
    @JsonIgnore
    private Foto fotoByFotoIdFoto;
}
