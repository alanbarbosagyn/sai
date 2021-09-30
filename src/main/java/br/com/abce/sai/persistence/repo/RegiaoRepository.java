package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Regiao;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RegiaoRepository extends CrudRepository<Regiao, Long> {

    Iterable<Regiao> findAllByMunicipio_CodgIbgeOrMunicipio_IdMunicipio(final String codgIbge, final Long idMunicipio);
}
