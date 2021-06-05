package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Endereco;
import org.springframework.data.repository.CrudRepository;

public interface EnderecoRepository extends CrudRepository<Endereco, Long> {
}
