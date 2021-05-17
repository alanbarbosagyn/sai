package br.com.abce.sai.persistence.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.abce.sai.persistence.model.Imovel;

public interface ImovelRepository extends CrudRepository<Imovel, Long> {
	
	List<Imovel> findByNome(String nome);
}
