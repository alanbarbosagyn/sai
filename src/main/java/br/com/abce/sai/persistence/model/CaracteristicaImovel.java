package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "caracteristica_imovel", schema = "sai", catalog = "")
public class CaracteristicaImovel {


    @Id
    @Column(name = "id_caracteristica_imovel", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCaracteristicaImovel;

    @NotNull(message = "A descrição é obrigatória")
    @Size(message = "Tamanho do campo descrição deve ser entre 3 e 45 caracteres", min = 3, max = 45)
    @Column(name = "descricao", nullable = true, length = 45)
    private String descricao;

    @NotEmpty(message = "O tipo é obrigatório.")
    @Pattern(message = "O tipo deve ser 1 - Boleano; 2 - Númerico, 3 - Descritivo.", regexp = "[1-3]?")
    @Column(name = "tipo", nullable = true)
    private String tipo;

    @Column(name = "icone", nullable = true, length = 45)
    private String icone;

    @Column(name = "sequencia", nullable = true)
    private Integer sequencia;

    @Column(name = "principal", nullable = true)
    private Byte principal;

//    @Column(name = "tipo_imovel_id_tipo_imovel", nullable = false)
//    private int tipoImovelIdTipoImovel;

    @ManyToOne
    @Valid
    @JoinColumn(name = "tipo_imovel_id_tipo_imovel", referencedColumnName = "id_tipo_imovel", nullable = false)
    private TipoImovel tipoImovelByTipoImovelIdTipoImovel;

//    @OneToMany(mappedBy = "caracteristicaImovelByCaracteristicaImovelIdCaracteristicaImovel")
//    @JsonIgnore
//    private Collection<ImovelHasCaracteristicaImovel> imovelHasCaracteristicaImovelsByIdCaracteristicaImovel;

}
