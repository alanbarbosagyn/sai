package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.CorretorImovelFavorito;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CorretorImovelFavoritoRepository extends CrudRepository<CorretorImovelFavorito, Long> {

    Optional<CorretorImovelFavorito> findById_ImovelIdAndId_CorretorId(Long idImovel, Long idCorretor);
    Iterable<CorretorImovelFavorito> findById_ImovelIdOrId_CorretorId(Long idImovel, Long idCorretor);
}
