package br.com.abce.sai.dto;

import br.com.abce.sai.persistence.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.sql.Date;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImovelDto {

    private Long idImovel;

    @NotBlank(message = "A descrição é obrigatório.")
    @Size(message = "A descrição deve até 200 caracteres", max = 200)
    private String descricao;

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMax(value = "999999999.99", message = "O valor não pode ser maior que 999999999.99.")
    @DecimalMin(value = "0.01", message = "O valor não pode ser 0,00 (zero).")
    private Double valor;

    @DecimalMax(value = "999999999.99", message = "O valor do imposto não pode ser maior que 999999999.99.")
    @DecimalMin(value = "0.01", message = "O valor do imposto não pode ser 0,00 (zero).")
    private Double valorImposto;

    @NotNull(message = "A área total é obrigatório.")
    @DecimalMax(value = "99999.99", message = "A área total não pode ser maior que 99999.99.")
    @DecimalMin(value = "0.01", message = "A área total não pode ser 0,00 (zero).")
    private Double areaTotal;

    @DecimalMax(value = "99999.99", message = "A área útil não pode ser maior que 99999.99.")
    @DecimalMin(value = "0.01", message = "A área útil não pode ser 0,00 (zero).")
    private Double areaUtil;

    private Date dataCadastro;
    private int status;

    @DecimalMax(value = "90.0", message = "A latitude não pode ser maior que 90.0.")
    @DecimalMin(value = "-90.0", message = "A latitude não pode ser menor que -90,0.")
    private Double localizacaoLatitude;

    @DecimalMax(value = "90.0", message = "A longitude não pode ser maior que 90.0.")
    @DecimalMin(value = "-90.0", message = "A latitude não pode ser menor que -90,0.")
    private Double localizacaoLongitude;

    @NotNull(message = "O tipo do imóvel é obrigatório.")
    @Valid
	private TipoImovel tipoImovelByTipoImovelIdTipoImovel;

    @NotNull(message = "O perfil do imóvel é obrigatório.")
    @Valid
	private PerfilImovel perfilImovelByPerfilImovelIdPerfilImovel;

    @Valid
    private Collection<Conveniencia> listaConveniencia;

    @NotNull(message = "Endereco é obrigatório")
    @Valid
    @OneToOne
    @JoinColumn(name = "endereco_id_endereco", referencedColumnName = "id_endereco", nullable = false)
    private Endereco enderecoByEnderecoIdEndereco;

    @NotNull(message = "Construtor é obrigatório")
    @Valid
    private ConstrutorDto construtor;

    @Valid
    private Collection<ImovelHasCaracteristicaImovel> imovelHasCaracteristicaImovelsByIdImovel;

//    @Valid
//    private Collection<CorretorDto> listaCorretorFavorito;
}
