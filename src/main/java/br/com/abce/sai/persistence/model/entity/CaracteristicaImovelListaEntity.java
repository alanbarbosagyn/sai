package br.com.abce.sai.persistence.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "caracteristica_imovel_lista", schema = "sai", catalog = "")
public class CaracteristicaImovelListaEntity {

    @Id
    @Column(name = "id_caracteristica_imovel_lista", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idCaracteristicaImovelLista;

    @Column(name = "valor", nullable = true, length = 45)
    private String valor;

//    @Column(name = "caracteristica_imovel_id_caracteristica_imovel1", nullable = false)
//    private int caracteristicaImovelIdCaracteristicaImovel1;

    @ManyToOne
    @JoinColumn(name = "caracteristica_imovel_id_caracteristica_imovel1", referencedColumnName = "id_caracteristica_imovel", nullable = false)
    private CaracteristicaImovelEntity caracteristicaImovelByCaracteristicaImovelIdCaracteristicaImovel1;

}
