package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Imovel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ImovelRepository extends CrudRepository<Imovel, Long> {
	
}
