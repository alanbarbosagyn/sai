package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conveniencia_has_imovel", schema = "sai")
//@IdClass(ConvenienciaHasImovelEntityPK.class)
public class ConvenienciaHasImovel {


    @EmbeddedId
    private ConvenienciaHasImovelPK id;

//    @Id
//    @Column(name = "conveniencia_id_conveniencia", nullable = false)
//    private int convenienciaIdConveniencia;
//
//    @Id
//    @Column(name = "imovel_id_imovel", nullable = false)
//    private int imovelIdImovel;

    @MapsId("convenienciaIdConveniencia")
    @ManyToOne
    @JoinColumn(name = "conveniencia_id_conveniencia", referencedColumnName = "id_conveniencia", nullable = false)
    @JsonBackReference
    @JsonIgnoreProperties
    @JsonIgnore
    private Conveniencia convenienciaByConvenienciaIdConveniencia;

    @MapsId("imovelIdImovel")
    @ManyToOne
    @JoinColumn(name = "imovel_id_imovel", referencedColumnName = "id_imovel", nullable = false)
    @JsonBackReference
    @JsonIgnoreProperties
    @JsonIgnore
    private Imovel imovelByImovelIdImovel;
}
