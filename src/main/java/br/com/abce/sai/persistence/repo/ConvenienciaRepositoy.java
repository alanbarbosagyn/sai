package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Conveniencia;
import org.springframework.data.repository.CrudRepository;

public interface ConvenienciaRepositoy extends CrudRepository<Conveniencia, Long> {

    Iterable<Conveniencia> findByDescricaoIsContaining(final String nome);
}
