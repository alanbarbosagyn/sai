package br.com.abce.sai.controller;

import br.com.abce.sai.dto.ImovelDto;
import br.com.abce.sai.dto.PesquisaImovelDto;
import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.ImovelSpecification;
import br.com.abce.sai.persistence.model.*;
import br.com.abce.sai.persistence.repo.*;
import br.com.abce.sai.representacao.ImovelModelAssembler;
import br.com.abce.sai.service.MunicipioService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://mvzrxz.hospedagemelastica.com.br", "https://feedimoveis.com.br"})
@RequestMapping("/api/imoveis")
@Api
public class ImovelController {

	private ImovelRepository imovelRepository;

	private final ImovelModelAssembler assembler;

	private final ModelMapper modelMapper;

	private final ConstrutorRepository construtorRepository;

	private final ImovelFotoRepository imovelFotoRepository;

	private final EnderecoRepository enderecoRepository;

	private FotoRepository fotoRepository;

	private RegiaoRepository regiaoRepository;

	private MunicipioService municipioService;

	public ImovelController(ImovelRepository imovelRepository, ImovelModelAssembler assembler, ModelMapper modelMapper, ConstrutorRepository construtorRepository, ImovelFotoRepository imovelFotoRepository, EnderecoRepository enderecoRepository, FotoRepository fotoRepository, RegiaoRepository regiaoRepository, MunicipioService municipioService) {
		this.imovelRepository = imovelRepository;
		this.assembler = assembler;
		this.modelMapper = modelMapper;
		this.construtorRepository = construtorRepository;
		this.imovelFotoRepository = imovelFotoRepository;
		this.enderecoRepository = enderecoRepository;
		this.fotoRepository = fotoRepository;
		this.regiaoRepository = regiaoRepository;
		this.municipioService = municipioService;
	}

	@ApiOperation(value = "Lista os im??vel.")
	@GetMapping
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
					value = "Pagina a ser carregada", defaultValue = "0"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
					value = "Quantidade de registros", defaultValue = "5"),
			@ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
					value = "Ordenacao dos registros")
	})
	public PagedModel<EntityModel<ImovelDto>> findAll(@SortDefault.SortDefaults({
															@SortDefault(sort = "dataCadastro", direction = Sort.Direction.DESC),
															@SortDefault(sort = "descricao", direction = Sort.Direction.ASC)
													}) @ApiIgnore Pageable pageable,
														   @ApiParam(name = "pesquisaImovelDto") PesquisaImovelDto pesquisaImovelDto) {

		Specification<Imovel> specification = new ImovelSpecification(pesquisaImovelDto);

		Page<Imovel> imovels = imovelRepository.findAll(specification, pageable);

		List<EntityModel<ImovelDto>> imovelsEntity = (imovels).stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

		PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(imovels.getSize(), imovels.getNumber(), imovels.getTotalElements(), imovels.getTotalPages());

		return PagedModel.of(imovelsEntity, metadata,
				linkTo(methodOn(ImovelController.class).findAll(pageable, pesquisaImovelDto)).withRel("imoveis"));
	}

	@ApiOperation(value = "Consulta im??vel por ID.")
	@GetMapping("{id}")
	public EntityModel<ImovelDto> findByOne(@PathVariable @NotNull(message = "Id do im??vel obrigat??rio.") Long id) {

		Imovel employee = imovelRepository.findById(id)
				.orElseThrow(() -> new RecursoNotFoundException(Imovel.class, id));

		return assembler.toModel(employee);
	}

	@ApiOperation(value = "Cria um im??vel.")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EntityModel<ImovelDto>> create(@RequestBody @Valid ImovelDto imovelDto) {

		Imovel imovel = getImovel(imovelDto);

		EntityModel<ImovelDto> imovelEntityModel = assembler.toModel(imovelRepository.save(imovel));

		return ResponseEntity.created(imovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
			.body(imovelEntityModel);
	}

	@ApiOperation(value = "Deleta um im??vel pelo ID.")
	@DeleteMapping("{id}")
	public HttpEntity<Object> delete(@PathVariable @NotNull(message = "Id do im??vel obrigat??rio.") Long id) {

		imovelRepository.findById(id)
        .orElseThrow(() -> new RecursoNotFoundException(Imovel.class, id));
		imovelRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Atualiza dados do im??vel.")
	@PutMapping("/{id}")
    public ResponseEntity<EntityModel<ImovelDto>> updateImovel(@RequestBody @Valid ImovelDto newImovel, @PathVariable Long id) {

		Imovel imovelUpdaded = imovelRepository.findById(id)
			.map(imovel -> {
				imovel = getImovel(newImovel);
				return imovelRepository.save(imovel);
			})
			.orElseGet(() -> {
				Imovel imovel = modelMapper.map(newImovel, Imovel.class);
				imovel.setIdImovel(id);
				return imovelRepository.save(imovel);
			});

        EntityModel<ImovelDto> imovelEntity = assembler.toModel(imovelUpdaded);

		return ResponseEntity
				.created(imovelEntity.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(imovelEntity);
    }

	@ApiOperation(value = "Salva foto de um im??vel.")
	@PostMapping("{id-imovel}/foto")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EntityModel<ImovelHasFoto>> createFoto(@RequestBody @Valid ImovelHasFoto imovelHasFoto,
															 @PathVariable(name = "id-imovel") @NotNull(message = "Id do im??vel obrigat??rio") Long idImovel) {

		Imovel imovel = imovelRepository.findById(idImovel)
				.orElseThrow(() -> new DataValidationException("Im??vel n??o encontrado."));

		Foto foto = fotoRepository.findById(imovelHasFoto.getId().getFotoIdFoto())
				.orElseThrow(() -> new DataValidationException("Foto n??o encontrada."));

		imovelHasFoto.setImovelByImovelIdImovel(imovel);
		imovelHasFoto.setId(new ImovelHasFotoPK(imovel.getIdImovel(), foto.getIdFoto()));
		imovelHasFoto.setFotoByFotoIdFoto(foto);

		EntityModel<ImovelHasFoto> imovelEntityModel = EntityModel.of(imovelFotoRepository.save(imovelHasFoto)
			,linkTo(methodOn(FotoController.class).findByOne(foto.getIdFoto(), null)).withSelfRel());

		return ResponseEntity.created(imovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(imovelEntityModel);
	}

	@ApiOperation(value = "Deleta foto do im??vel.")
	@DeleteMapping("{id-imovel}/foto/{id-foto}")
	public HttpEntity<Object> delete(@PathVariable(name = "id-imovel") @NotNull(message = "Id do im??vel ?? obrigat??rio.") Long idImovel,
									 @PathVariable(name = "id-foto") @NotNull(message = "Id da foto ?? obrigat??rio.") Long idFoto) {

		ImovelHasFoto fotoImovelDelete = imovelFotoRepository.findImovelHasFotosById_ImovelIdImovelAndId_FotoIdFoto(idImovel, idFoto)
				.orElseThrow(() -> new RecursoNotFoundException(ImovelHasFoto.class, 0L));

		imovelFotoRepository.delete(fotoImovelDelete);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Lista as fotos de um im??vel.")
	@GetMapping("{id-imovel}/foto")
	public Collection<EntityModel<ImovelHasFoto>> findAllFoto(@PathVariable(name = "id-imovel") @NotNull(message = "Id do im??vel obrigat??rio") Long idImovel) {

		Imovel imovel = imovelRepository.findById(idImovel)
				.orElseThrow(() -> new DataValidationException("Im??vel n??o encontrado."));

		Collection<EntityModel<ImovelHasFoto>> imovelHasFotoCollection = new ArrayList<>();

		for (ImovelHasFoto imovelHasFoto : imovelFotoRepository.findImovelHasFotosById_ImovelIdImovel(idImovel)) {
			EntityModel<ImovelHasFoto> imoveis = EntityModel.of(imovelHasFoto,
					linkTo(methodOn(FotoController.class)
							.findByOne(imovelHasFoto.getId().getFotoIdFoto(), Const.MIN_WITH)).withSelfRel(),
					linkTo(methodOn(ImovelController.class)
							.findAllFoto(imovelHasFoto.getId().getImovelIdImovel())).withRel("fotos-imovel"));
			imovelHasFotoCollection.add(imoveis);
		}

		return imovelHasFotoCollection;
	}

	private Imovel getImovel(ImovelDto imovelDto) {

		Imovel imovel = modelMapper.map(imovelDto, Imovel.class);

		imovel.setDataCadastro(new Date());
		imovel.setStatus(1);

		Construtor construtor = construtorRepository.findById(imovelDto.getConstrutor().getId())
				.orElseThrow(() -> new DataValidationException("Construtor n??o cadastrado."));
		imovel.setConstrutorByConstrutorIdConstrutor(construtor);

		Regiao regiao = regiaoRepository.findById(imovelDto.getRegiaoImovel().getIdRegiao())
				.orElseThrow(() -> new DataValidationException("Regi??o n??o cadastrada."));
		imovel.setRegiaoImovel(regiao);

		Endereco enderecoIdEndereco = imovelDto.getEnderecoByEnderecoIdEndereco();

		if (enderecoIdEndereco != null) {

			Endereco enderecoBase;

			if (enderecoIdEndereco.getIdEndereco() != null) {
				Optional<Endereco> endereco = enderecoRepository.findById(enderecoIdEndereco.getIdEndereco());
				enderecoBase = endereco.orElseGet(Endereco::new);
			} else {
				enderecoBase = new Endereco();
			}

			enderecoBase.setUf(enderecoIdEndereco.getUf());
			enderecoBase.setNumero(enderecoIdEndereco.getNumero());
			enderecoBase.setLogradouro(enderecoIdEndereco.getLogradouro());
			enderecoBase.setComplemento(enderecoIdEndereco.getComplemento());
			enderecoBase.setCidade(enderecoIdEndereco.getCidade());
			enderecoBase.setCep(enderecoIdEndereco.getCep());
			enderecoBase.setBairro(enderecoIdEndereco.getBairro());
			enderecoBase.setMunicipio(municipioService.getMunicipio(enderecoIdEndereco.getMunicipio()));
			imovel.setEnderecoByEnderecoIdEndereco(enderecoBase);
		}

		if (imovelDto.getListaConveniencia() != null && !imovelDto.getListaConveniencia().isEmpty()) {
			Collection<ConvenienciaHasImovel> convenienciaHasImovels = new ArrayList<>();
			for (Conveniencia conveniencia : imovelDto.getListaConveniencia()) {
				ConvenienciaHasImovel convenienciaHasImovel = new ConvenienciaHasImovel();
				convenienciaHasImovel.setImovelByImovelIdImovel(imovel);
				convenienciaHasImovel.setConvenienciaByConvenienciaIdConveniencia(conveniencia);
				convenienciaHasImovel.setId(new ConvenienciaHasImovelPK(conveniencia.getIdConveniencia(), imovel.getIdImovel()));
				convenienciaHasImovels.add(convenienciaHasImovel);
			}

			imovel.setConvenienciaHasImovelsByIdImovel(convenienciaHasImovels);
		}

		for (ImovelHasCaracteristicaImovel imovelHasCaracteristicaImovel : imovel.getImovelHasCaracteristicaImovelsByIdImovel()) {
			CaracteristicaImovel caracteristicaImovel = imovelHasCaracteristicaImovel.getCaracteristicaImovelByCaracteristicaImovelIdCaracteristicaImovel();
			imovelHasCaracteristicaImovel.setId(new ImovelHasCaracteristicaImovelPK(imovel.getIdImovel(), caracteristicaImovel.getIdCaracteristicaImovel()));
			imovelHasCaracteristicaImovel.setImovelByImovelIdImovel(imovel);
		}
		return imovel;
	}
}
