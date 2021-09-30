package br.com.abce.sai.persistence.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Getter
@Setter
@Entity
@Table(name = "municipio", schema = "sai")
public class Municipio {

    @Id
    @Column(name = "id_municipio", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMunicipio;

    @Column(name = "nome_municipio", nullable = false, length = 50)
    private String nomeMunicipio;

    @Column(name = "longitude", nullable = false, precision = 3, scale = 8)
    @DecimalMax(value = "180.0", message = "A longitude não pode ser maior que 180.0.")
    @DecimalMin(value = "-180.0", message = "A latitude não pode ser menor que -180,0.")
    private Double longitude;

    @Column(name = "latitude", nullable = false, precision = 2, scale = 8)
    @DecimalMax(value = "90.0", message = "A latitude não pode ser maior que 90.0.")
    @DecimalMin(value = "-90.0", message = "A latitude não pode ser menor que -90,0.")
    private Double latitude;

    @Column(name = "uf_estado", nullable = false, length = 2)
    private String ufEstado;

    @Column(name = "codg_ibge", nullable = false, length = 7)
    private String codgIbge;
}