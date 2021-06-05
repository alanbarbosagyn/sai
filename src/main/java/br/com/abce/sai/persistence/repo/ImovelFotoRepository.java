package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.ImovelHasFoto;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface ImovelFotoRepository extends CrudRepository<ImovelHasFoto, Long> {

    Collection<ImovelHasFoto> findImovelHasFotosById_ImovelIdImovel(final Long idImovel);

    Collection<ImovelHasFoto> findImovelHasFotosById_ImovelIdImovelAndCapa(final Long idImovel, boolean isCapa);

    Optional<ImovelHasFoto> findTopByOrdem(final Long idImovel);

    Collection<ImovelHasFoto> findImovelHasFotosById_ImovelIdImovelAndOrdem(final Long idImovel, final Integer idOrdem);
}
