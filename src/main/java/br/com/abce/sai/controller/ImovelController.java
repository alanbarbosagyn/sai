package br.com.abce.sai.controller;

import br.com.abce.sai.exception.ResourcedMismatchException;
import br.com.abce.sai.exception.RecursoNotFoundException;
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

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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

		List<EntityModel<Imovel>> employees = ((List<Imovel>) imovelRepository.findAll()).stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(employees);
	}
	
	@ApiOperation(value = "Consulta imóvel por ID.")
	@GetMapping("{id}")
	public EntityModel<Imovel> findByOne(@PathVariable @NotNull(message = "Id do imóvel obrigatório.") Long id) {

		Imovel employee = imovelRepository.findById(id)
				.orElseThrow(() -> new RecursoNotFoundException(Imovel.class, id));

		return assembler.toModel(employee);
	}

	@ApiOperation(value = "Cria um imóvel.")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<EntityModel<Imovel>> create(@RequestBody Imovel imovel) {

		EntityModel<Imovel> imovelEntityModel = assembler.toModel(imovelRepository.save(imovel));

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
    public ResponseEntity<EntityModel<Imovel>> updateImovel(@RequestBody Imovel newImovel, @PathVariable Long id) {
        if (newImovel.getIdImovel() != null && newImovel.getIdImovel().equals(id)) {
          throw new ResourcedMismatchException(id);
        }

        Imovel imovelUpdaded = imovelRepository.findById(id)
			.map(imovel -> {
				imovel.setDescricao(newImovel.getDescricao());
				imovel.setValor(newImovel.getValor());
				return imovelRepository.save(imovel);
			})
			.orElseGet(() -> {
				newImovel.setIdImovel(id);
				return imovelRepository.save(newImovel);
			});

        EntityModel<Imovel> imovelEntity = assembler.toModel(imovelUpdaded);

		return ResponseEntity
				.created(imovelEntity.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(imovelEntity);
    }
}
