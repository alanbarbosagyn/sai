package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Municipio;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MunicipioRepository extends CrudRepository<Municipio, Long> {

    Iterable<Municipio> findByUfEstado(final String ufEstado);

    Iterable<Municipio> findByUfEstadoAndNomeMunicipioIsContaining(final String ufEstado, final String nomeMunicipio);

    Optional<Municipio> findMunicipioByIdMunicipioOrCodgIbge(final Long idMunicipio, final String codgIbge);
}
