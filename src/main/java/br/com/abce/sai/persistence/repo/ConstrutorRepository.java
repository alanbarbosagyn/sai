package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Construtor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConstrutorRepository extends CrudRepository<Construtor, Long> {

    Optional<Construtor> findByCnpjOrUsuarioByUsuarioIdUsuario_IdUsuario(final String cnpj, final Long usuarioId);

    boolean existsByCnpj(final String cnpj);

}
