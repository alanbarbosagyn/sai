package br.com.abce.sai.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tipo_imovel", schema = "sai")
public class TipoImovel {

    @Id
    @Column(name = "id_tipo_imovel", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipoImovel;

    @NotNull(message = "A descrição não pode ser nulo.")
    @Size(message = "A descrição deve onter entre 10 e 45 caracteres.", min = 4, max = 45)
    @Column(name = "descricao", nullable = false, length = 45)
    private String descricao;

//    @OneToMany(mappedBy = "tipoImovelByTipoImovelIdTipoImovel")
//    private Collection<CaracteristicaImovelEntity> caracteristicaImovelsByIdTipoImovel;

//    @OneToMany(mappedBy = "tipoImovelByTipoImovelIdTipoImovel")
//    private Collection<Imovel> imovelsByIdTipoImovel;
}
