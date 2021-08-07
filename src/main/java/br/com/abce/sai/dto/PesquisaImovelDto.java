package br.com.abce.sai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PesquisaImovelDto {

    private Long[] idTipo;
    private Long[] idPerfil;
    private Long[] idConveniencias;
    private Long[] idConstrutores;
    private Long[] idCorretores;

    private Map<Long,String> caracteristica;

    private String cidade;
    private String bairro;

    private Double valorMinimo;
    private Double valorMaximo;

    private Double areaConstruidaMinima;
    private Double areaConstruidaMaxima;

    private Double areaTotalMinima;
    private Double areaTotalMaxima;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class CaracteristicaPesquisaDto {
        private Long idCaracteristica;
        private String valeu;
    }
}
