package br.com.abce.sai.controller;

import br.com.abce.sai.dto.ImovelDto;
import br.com.abce.sai.exception.DataValidationException;
import br.com.abce.sai.exception.RecursoNotFoundException;
import br.com.abce.sai.exception.ResourcedMismatchException;
import br.com.abce.sai.persistence.model.*;
import br.com.abce.sai.persistence.repo.ConstrutorRepository;
import br.com.abce.sai.persistence.repo.FotoRepository;
import br.com.abce.sai.persistence.repo.ImovelFotoRepository;
import br.com.abce.sai.persistence.repo.ImovelRepository;
import br.com.abce.sai.representacao.ImovelModelAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/imoveis")
@Api
@CrossOrigin(origins = "http://localhost:4200")
public class ImovelController {

	private ImovelRepository imovelRepository;

	private final ImovelModelAssembler assembler;

	private final ModelMapper modelMapper;

	private final ConstrutorRepository construtorRepository;

	private final ImovelFotoRepository imovelFotoRepository;

	private FotoRepository fotoRepository;

	public ImovelController(ImovelRepository imovelRepository, ImovelModelAssembler assembler, ModelMapper modelMapper, ConstrutorRepository construtorRepository, ImovelFotoRepository imovelFotoRepository, br.com.abce.sai.persistence.repo.FotoRepository fotoRepository) {
		this.imovelRepository = imovelRepository;
		this.assembler = assembler;
		this.modelMapper = modelMapper;
		this.construtorRepository = construtorRepository;
		this.imovelFotoRepository = imovelFotoRepository;
		this.fotoRepository = fotoRepository;
	}

	@ApiOperation(value = "Lista os imóvel.")
	@GetMapping
	public CollectionModel<EntityModel<ImovelDto>> findAll() {

		List<EntityModel<ImovelDto>> employees = ((List<Imovel>) imovelRepository.findAll()).stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(employees);
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
        if (newImovel.getIdImovel() != null && newImovel.getIdImovel().equals(id)) {
          throw new ResourcedMismatchException(id);
        }

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
			,linkTo(methodOn(FotoController.class).findByOne(foto.getIdFoto(), null, null)).withSelfRel());

		return ResponseEntity.created(imovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(imovelEntityModel);
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
							.findByOne(imovelHasFoto.getId().getFotoIdFoto(), Const.MIN_WITH, Const.MIN_HEIGHT)).withSelfRel(),
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
