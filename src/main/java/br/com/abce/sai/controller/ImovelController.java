package br.com.abce.sai.controller;

import br.com.abce.sai.exception.ImovelIdMismatchException;
import br.com.abce.sai.exception.ImovelNotFoundException;
import br.com.abce.sai.persistence.model.Imovel;
import br.com.abce.sai.persistence.repo.ImovelRepository;
import br.com.abce.sai.representacao.ImovelModelAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/imoveis")
@Api
@CrossOrigin(origins = "http://localhost:4200")
public class ImovelController {

	private final ImovelRepository imovelRepository;

	private final ImovelModelAssembler assembler;

	public ImovelController(ImovelRepository imovelRepository, ImovelModelAssembler assembler) {
		this.imovelRepository = imovelRepository;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<Imovel>> findAll() {
//		return imovelRepository.findAll();
//		List<EntityModel<Imovel>> imoveis = (List<EntityModel<Imovel>>) ((List) imovelRepository.findAll()).stream()
//				.map(imovel -> EntityModel.of(imovel,
//						linkTo(methodOn(ImovelController.class).findByOne(((Imovel) imovel).getId())).withSelfRel(),
//						linkTo(methodOn(ImovelController.class).findAll()).withRel("employees")))
//				.collect(Collectors.toList());
//
//		return CollectionModel.of(imoveis, linkTo(methodOn(ImovelController.class).findAll()).withSelfRel());

//		List<EntityModel<Imovel>> employees = ((List<Imovel>) imovelRepository.findAll()).stream()
//				.map(assembler::toModel) //
//				.collect(Collectors.toList());
//
//		return CollectionModel.of(employees, linkTo(methodOn(ImovelController.class).findAll()).withSelfRel());

		List<EntityModel<Imovel>> employees = ((List<Imovel>) imovelRepository.findAll()).stream() //
				.map(assembler::toModel) //
				.collect(Collectors.toList());

		return CollectionModel.of(employees);
	}
	
	@ApiOperation(value = "Consulta imóvel por ID.")
	@GetMapping("{id}")
	public EntityModel<Imovel> findByOne(@PathVariable Long id) {
//		Imovel imove = imovelRepository.findById(id).orElseThrow(() -> new ImovelNotFoundException(id));
//
//		return EntityModel.of(imove,
//				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ImovelController.class).findByOne(id)).withSelfRel(),
//				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ImovelController.class).findAll()).withRel("imoveis"));

		Imovel employee = imovelRepository.findById(id)
				.orElseThrow(() -> new ImovelNotFoundException(id));

		return assembler.toModel(employee);
	}

	@ApiOperation(value = "Cria um imóvel.")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EntityModel<Imovel>> create(@RequestBody Imovel imovel) {
//		return imovelRepository.save(imovel);

		EntityModel<Imovel> imovelEntityModel = assembler.toModel(imovelRepository.save(imovel));

		return ResponseEntity.created(imovelEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
			.body(imovelEntityModel);
	}

	@ApiOperation(value = "Deleta um imóvel pelo ID.")
	@DeleteMapping("{id}")
	public HttpEntity<Object> delete(@PathVariable Long id) {

		imovelRepository.findById(id)
        .orElseThrow(() -> new ImovelNotFoundException(id));
		imovelRepository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Atualiza dados do imóvel.")
	@PutMapping("/{id}")
    public ResponseEntity<EntityModel<Imovel>> updateImovel(@RequestBody Imovel newImovel, @PathVariable Long id) {
        if (newImovel.getId() != null && newImovel.getId().equals(id)) {
          throw new ImovelIdMismatchException();
        }

        Imovel imovelUpdaded = imovelRepository.findById(id)
			.map(imovel -> {
				imovel.setNome(newImovel.getNome());
				imovel.setValor(newImovel.getValor());
				return imovelRepository.save(imovel);
			})
			.orElseGet(() -> {
				newImovel.setId(id);
				return imovelRepository.save(newImovel);
			});

        EntityModel<Imovel> imovelEntity = assembler.toModel(imovelUpdaded);

//        return imovelRepository.save(imovel);
//		return assembler.toModel(imovelRepository.save(newImovel));
		return ResponseEntity
				.created(imovelEntity.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(imovelEntity);
    }
}
