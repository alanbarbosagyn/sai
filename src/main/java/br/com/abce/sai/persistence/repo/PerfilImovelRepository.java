package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.PerfilImovel;
import org.springframework.data.repository.CrudRepository;

public interface PerfilImovelRepository extends CrudRepository<PerfilImovel, Long> {

    PerfilImovel findByDescricao(final String descricao);
}
