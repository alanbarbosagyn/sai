package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Corretor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CorretorRepository extends CrudRepository<Corretor, Long> {

    Optional<Corretor> findByNumCreciEquals(final String numero);

    Optional<Corretor> findByCpfEquals(final String cpf);

    Optional<Corretor> findByNumCreciOrCpfOrUsuarioByUsuarioIdUsuario_IdUsuario(final String numCreci, final String cpf, final Long usuarioId);
}
