package br.com.abce.sai.controller;

import br.com.abce.sai.dto.ImovelDto;
import br.com.abce.sai.dto.PesquisaImovelDto;
import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.persistence.ImovelSpecification;
import br.com.abce.sai.persistence.model.*;
import br.com.abce.sai.persistence.repo.*;
import br.com.abce.sai.representacao.ImovelModelAssembler;
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

	public ImovelController(ImovelRepository imovelRepository, ImovelModelAssembler assembler, ModelMapper modelMapper, ConstrutorRepository construtorRepository, ImovelFotoRepository imovelFotoRepository, EnderecoRepository enderecoRepository, FotoRepository fotoRepository) {
		this.imovelRepository = imovelRepository;
		this.assembler = assembler;
		this.modelMapper = modelMapper;
		this.construtorRepository = construtorRepository;
		this.imovelFotoRepository = imovelFotoRepository;
		this.enderecoRepository = enderecoRepository;
		this.fotoRepository = fotoRepository;
	}

	@ApiOperation(value = "Lista os imóvel.")
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

	@ApiOperation(value = "Consulta imóvel por ID.")
	@GetMapping("{id}")
	public EntityModel<ImovelDto> findByOne(@PathVariable @NotNull(message = "Id do imóvel obrigatório.") Long id) {

		Imovel employee = imovelRepository.findById(id)
				.orElseThrow(() -> new RecursoNotFoundException(Imovel.class, id));

		return assembler.toModel(employee);
	}

	@ApiOperation(value = "Cria um imóvel.")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EntityModel<ImovelDto>> create(@RequestBody @Valid ImovelDto imovelDto) {

		Imovel imovel = getImovel(imovelDto);

		EntityModel<ImovelDto> imovelEntityModel = assembler.toModel(imovelRepository.save(imovel));

		return ResponseEntity.created(imovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
			.body(imovelEntityModel);
	}

	@ApiOperation(value = "Deleta um imóvel pelo ID.")
	@DeleteMapping("{id}")
	public HttpEntity<Object> delete(@PathVariable @NotNull(message = "Id do imóvel obrigatório.") Long id) {

		imovelRepository.findById(id)
        .orElseThrow(() -> new RecursoNotFoundException(Imovel.class, id));
		imovelRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Atualiza dados do imóvel.")
	@PutMapping("/{id}")
    public ResponseEntity<EntityModel<ImovelDto>> updateImovel(@RequestBody @Valid ImovelDto newImovel, @PathVariable Long id) {
//        if (newImovel.getIdImovel() != null && newImovel.getIdImovel().equals(id)) {
//          throw new ResourcedMismatchException(id);
//        }

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

	@ApiOperation(value = "Salva foto de um imóvel.")
	@PostMapping("{id-imovel}/foto")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EntityModel<ImovelHasFoto>> createFoto(@RequestBody @Valid ImovelHasFoto imovelHasFoto,
															 @PathVariable(name = "id-imovel") @NotNull(message = "Id do imóvel obrigatório") Long idImovel) {

		Imovel imovel = imovelRepository.findById(idImovel)
				.orElseThrow(() -> new DataValidationException("Imóvel não encontrado."));

		Foto foto = fotoRepository.findById(imovelHasFoto.getId().getFotoIdFoto())
				.orElseThrow(() -> new DataValidationException("Foto não encontrada."));

//		imovelFotoRepository.findTopByOrdem(imovelHasFoto.getId().getImovelIdImovel())
//			.ifPresent();
//
//		if (maxOrdemFoto.isPresent())
//			imovelHasFoto.setOrdem(maxOrdemFoto.orElse()get().getOrdem()++);
//
//		if (imovelHasFoto.getOrdem() != null && imovelHasFoto.getOrdem() > 0) {
//			ImovelHasFoto fotoOrdem = imovelFotoRepository.findImovelHasFotosById_ImovelIdImovelAndOrdem(idImovel, imovelHasFoto.getOrdem());
//			fotoOrdem.setOrdem();
//		} else {
//
//		}


		imovelHasFoto.setImovelByImovelIdImovel(imovel);
		imovelHasFoto.setId(new ImovelHasFotoPK(imovel.getIdImovel(), foto.getIdFoto()));
//		imovelHasFoto.setOrdem();
		imovelHasFoto.setFotoByFotoIdFoto(foto);

		EntityModel<ImovelHasFoto> imovelEntityModel = EntityModel.of(imovelFotoRepository.save(imovelHasFoto)
			,linkTo(methodOn(FotoController.class).findByOne(foto.getIdFoto(), null)).withSelfRel());

		return ResponseEntity.created(imovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(imovelEntityModel);
	}

	@ApiOperation(value = "Deleta foto do imóvel.")
	@DeleteMapping("{id-imovel}/foto/{id-foto}")
	public HttpEntity<Object> delete(@PathVariable(name = "id-imovel") @NotNull(message = "Id do imóvel é obrigatório.") Long idImovel,
									 @PathVariable(name = "id-foto") @NotNull(message = "Id da foto é obrigatório.") Long idFoto) {

		ImovelHasFoto fotoImovelDelete = imovelFotoRepository.findImovelHasFotosById_ImovelIdImovelAndId_FotoIdFoto(idImovel, idFoto)
				.orElseThrow(() -> new RecursoNotFoundException(ImovelHasFoto.class, 0L));

		imovelFotoRepository.delete(fotoImovelDelete);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Lista as fotos de um imóvel.")
	@GetMapping("{id-imovel}/foto")
	public Collection<EntityModel<ImovelHasFoto>> findAllFoto(@PathVariable(name = "id-imovel") @NotNull(message = "Id do imóvel obrigatório") Long idImovel) {

		Imovel imovel = imovelRepository.findById(idImovel)
				.orElseThrow(() -> new DataValidationException("Imóvel não encontrado."));

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
				.orElseThrow(() -> new DataValidationException("Construtor não cadastrado."));
		imovel.setConstrutorByConstrutorIdConstrutor(construtor);

		Endereco enderecoIdEndereco = imovelDto.getEnderecoByEnderecoIdEndereco();

		if (enderecoIdEndereco != null) {

			Optional<Endereco> endereco = enderecoRepository.findById(enderecoIdEndereco.getIdEndereco());

			Endereco enderecoBase = endereco.isPresent() ? new Endereco() : endereco.get();

			enderecoBase.setUf(enderecoIdEndereco.getUf());
			enderecoBase.setNumero(enderecoIdEndereco.getNumero());
			enderecoBase.setLogradouro(enderecoIdEndereco.getLogradouro());
			enderecoBase.setComplemento(enderecoIdEndereco.getComplemento());
			enderecoBase.setCidade(enderecoIdEndereco.getCidade());
			enderecoBase.setCep(enderecoIdEndereco.getCep());
			enderecoBase.setBairro(enderecoIdEndereco.getBairro());

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
