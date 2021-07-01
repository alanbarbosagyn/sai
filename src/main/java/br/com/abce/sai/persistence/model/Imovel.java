package br.com.abce.sai.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "imovel", schema = "sai", catalog = "")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "idImovel")
public class Imovel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_imovel", nullable = false)
	private Long idImovel;

	@NotBlank(message = "A descrição é obrigatório.")
	@Size(message = "A descrição deve até 200 caracteres", max = 200)
	@Column(name = "descricao", nullable = false, length = 200)
	private String descricao;

	@NotNull(message = "O valor é obrigatório.")
	@DecimalMax(value = "999999999.99", message = "O valor não pode ser maior que 999999999.99.")
	@DecimalMin(value = "0.01", message = "O valor não pode ser 0,00 (zero).")
	@Column(name = "valor", nullable = false, precision = 11, scale = 2)
	private Double valor;

	@DecimalMax(value = "999999999.99", message = "O valor do imposto não pode ser maior que 999999999.99.")
	@DecimalMin(value = "0.01", message = "O valor do imposto não pode ser 0,00 (zero).")
	@Column(name = "valor_imposto", nullable = true, precision = 11, scale = 2)
	private Double valorImposto;

	@NotNull(message = "A área total é obrigatório.")
	@DecimalMax(value = "99999.99", message = "A área total não pode ser maior que 99999.99.")
	@DecimalMin(value = "0.01", message = "A área total não pode ser 0,00 (zero).")
	@Column(name = "area_total", nullable = false, precision = 7, scale = 2)
	private Double areaTotal;

	@DecimalMax(value = "99999.99", message = "A área útil não pode ser maior que 99999.99.")
	@DecimalMin(value = "0.01", message = "A área útil não pode ser 0,00 (zero).")
	@Column(name = "area_util", nullable = true, precision = 7, scale = 2)
	private Double areaUtil;

	@Column(name = "data_cadastro", nullable = false)
	private Date dataCadastro;

	@Column(name = "status", nullable = false)
	private int status;

	@DecimalMax(value = "90.0", message = "A latitude não pode ser maior que 90.0.")
	@DecimalMin(value = "-90.0", message = "A latitude não pode ser menor que -90,0.")
	@Column(name = "localizacao_latitude", nullable = true, precision = 2, scale = 7)
	private Double localizacaoLatitude;

	@DecimalMax(value = "90.0", message = "A longitude não pode ser maior que 90.0.")
	@DecimalMin(value = "-90.0", message = "A latitude não pode ser menor que -90,0.")
	@Column(name = "localizacao_longitude", nullable = true, precision = 2, scale = 7)
	private Double localizacaoLongitude;

	@URL(message = "Url inválida.")
	@Column(name = "url_video", length = 100)
	private String urlVideo;

//	@Column(name = "construtor_id_construtor", nullable = false)
//	private int construtorIdConstrutor;
//
//	@Column(name = "endereco_id_endereco", nullable = false)
//	private int enderecoIdEndereco;
//
//	@Column(name = "tipo_imovel_id_tipo_imovel", nullable = false)
//	private int tipoImovelIdTipoImovel;
//
//	@Column(name = "perfil_imovel_id_perfil_imovel", nullable = false)
//	private int perfilImovelIdPerfilImovel;

	@OneToMany(mappedBy = "imovelByImovelIdImovel", cascade = CascadeType.ALL)
	private Collection<ConvenienciaHasImovel> convenienciaHasImovelsByIdImovel;

	@NotNull(message = "Construtor do imóvel é obrigatório")
	@Valid
	@ManyToOne
	@JoinColumn(name = "construtor_id_construtor", referencedColumnName = "id_construtor", nullable = false)
	private Construtor construtorByConstrutorIdConstrutor;

	@NotNull(message = "endereco é obrigatório")
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id_endereco", referencedColumnName = "id_endereco", nullable = false)
	private Endereco enderecoByEnderecoIdEndereco;

	@NotNull(message = "tipo de imóvel é obrigatório")
	@Valid
	@ManyToOne
	@JoinColumn(name = "tipo_imovel_id_tipo_imovel", referencedColumnName = "id_tipo_imovel", nullable = false)
	private TipoImovel tipoImovelByTipoImovelIdTipoImovel;

	@NotNull(message = "perfil de imóvel é obrigatório")
	@Valid
	@ManyToOne
	@JoinColumn(name = "perfil_imovel_id_perfil_imovel", referencedColumnName = "id_perfil_imovel", nullable = false)
	private PerfilImovel perfilImovelByPerfilImovelIdPerfilImovel;

	@Valid
	@OneToMany(mappedBy = "imovelByImovelIdImovel", cascade = CascadeType.ALL)
	private Collection<ImovelHasCaracteristicaImovel> imovelHasCaracteristicaImovelsByIdImovel;

	@Valid
	@OneToMany(mappedBy = "imovelByImovelIdImovel")
	private Collection<ImovelHasFoto> imovelHasFotosByIdImovel;

//	@Valid
//	@OneToMany(mappedBy = "imovelByImovelId")
//	private Collection<CorretorImovelFavorito> imovelHasCorretorFavorito;
}
