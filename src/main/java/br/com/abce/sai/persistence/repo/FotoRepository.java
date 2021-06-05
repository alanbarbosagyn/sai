package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Foto;
import org.springframework.data.repository.CrudRepository;

public interface FotoRepository extends CrudRepository<Foto, Long> {
}
