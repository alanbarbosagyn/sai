package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.TipoImovel;
import org.springframework.data.repository.CrudRepository;

public interface TipoImoveRepository extends CrudRepository<TipoImovel, Long> {

    TipoImovel findByDescricao(final String descricao);
}
