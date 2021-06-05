package br.com.abce.sai.dto;

import br.com.abce.sai.persistence.model.CaracteristicaImovel;
import br.com.abce.sai.persistence.model.Conveniencia;
import br.com.abce.sai.persistence.model.PerfilImovel;
import br.com.abce.sai.persistence.model.TipoImovel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PesquisaImovelDto {

    private Collection<TipoImovel> tipoImovel;
    private Collection<PerfilImovel> pefilImovel;
    private Collection<Conveniencia> listaConveniencia;
    private Collection<ConstrutorDto> construtorDto;
    private Collection<CaracteristicaImovel> listaCaracteristica;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date dataCadastro;
    private String cidade;
    private String bairro;
    private String uf;

    private Double valorMinimo;
    private Double valorMaximo;

    private Double areaConstruidaMinima;
    private Double areaConstruidaMaxima;

    private Double areaTotalMinima;
    private Double areaTotalMaxima;
}
