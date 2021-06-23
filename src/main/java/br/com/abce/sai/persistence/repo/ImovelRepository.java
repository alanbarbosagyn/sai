package br.com.abce.sai.persistence.repo;

import br.com.abce.sai.persistence.model.Imovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ImovelRepository extends PagingAndSortingRepository<Imovel, Long>, JpaSpecificationExecutor<Imovel> {

    Page<Imovel> findAllByStatusEquals(Pageable pageable, Integer status);
}
