package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.CaracteristicaImovel;
import org.springframework.data.repository.CrudRepository;

public interface CaracteristicaImovelRepository extends CrudRepository<CaracteristicaImovel, Long> {

    Iterable<CaracteristicaImovel> findByDescricaoIsContaining(final String nome);
}
