package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Construtor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConstrutorRepository extends CrudRepository<Construtor, Long> {

    Optional<Construtor> findByCnpj(final String cnpj);

    boolean existsByCnpj(final String cnpj);

}
