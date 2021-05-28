package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Corretor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CorretorRepository extends CrudRepository<Corretor, Long> {

    boolean findByNumCreciEquals(final String numero);

    boolean findByCpfEquals(final String cpf);

    Optional<Corretor> findByNumCreciOrCpf(final String numCreci, final String cpf);
}
