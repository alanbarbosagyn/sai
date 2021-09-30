package br.com.abce.sai.persistence.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@Entity
@Table(name = "regiao", schema = "sai")
public class Regiao {

    @Id
    @Column(name = "id_regiao", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegiao;

    @NotNull(message = "Nome da região é obrigatório")
    @Size(max = 45, message = "Nome da região deve possuir até 45 caracteres")
    @Column(name = "nome_regiao", nullable = false, length = 45)
    private String nomeRegiao;

    @NotNull(message = "Longitude é obrigatório")
    @DecimalMax(value = "180.0", message = "A longitude não pode ser maior que 180.0.")
    @DecimalMin(value = "-180.0", message = "A latitude não pode ser menor que -180,0.")
    @Column(name = "longitude", nullable = false, precision = 3, scale = 8)
    private Double longitude;

    @NotNull(message = "Latitude é obrigatório")
    @Column(name = "latitude", nullable = false, precision = 3, scale = 8)
    @DecimalMax(value = "90.0", message = "A latitude não pode ser maior que 90.0.")
    @DecimalMin(value = "-90.0", message = "A latitude não pode ser menor que -90,0.")
    private Double latitude;

    @NotNull(message = "Situação da região é obrigatório.")
    @Column(name = "status", nullable = false)
    private Integer situacao;

    @JoinColumn(name = "municipio_id_municipio", referencedColumnName = "id_municipio")
    @ManyToOne(cascade = CascadeType.DETACH, optional = false)
    private Municipio municipio;
}
