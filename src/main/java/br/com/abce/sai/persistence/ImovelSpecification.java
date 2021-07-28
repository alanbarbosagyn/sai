package br.com.abce.sai.persistence;

import br.com.abce.sai.dto.PesquisaImovelDto;
import br.com.abce.sai.persistence.model.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ImovelSpecification implements Specification<Imovel> {

    private PesquisaImovelDto pesquisaImovelDto;

    @Override
    public Predicate toPredicate(Root<Imovel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> list = new ArrayList<>();

        query.distinct(true);

        Join<Endereco, Imovel> enderecoImovelJoin = root.join("enderecoByEnderecoIdEndereco", JoinType.INNER);
        if (StringUtils.isNotBlank(pesquisaImovelDto.getBairro())) {
            list.add(cb.like(enderecoImovelJoin.get("bairro").as(String.class), "%"+pesquisaImovelDto.getBairro() +"%"));
        }
        if (StringUtils.isNotBlank(pesquisaImovelDto.getCidade())) {
            list.add(cb.like(enderecoImovelJoin.get("cidade").as(String.class), "%"+pesquisaImovelDto.getCidade() +"%"));
        }

        if (pesquisaImovelDto.getAreaConstruidaMaxima() != null && pesquisaImovelDto.getAreaConstruidaMaxima() > 0) {
            list.add(cb.lt(root.get("areaUtil").as(Double.class), pesquisaImovelDto.getAreaConstruidaMaxima()));
        }

        if (pesquisaImovelDto.getAreaConstruidaMinima() != null && pesquisaImovelDto.getAreaConstruidaMinima() > 0) {
            list.add(cb.lt(root.get("areaUtil").as(Double.class), pesquisaImovelDto.getAreaConstruidaMinima()));
        }

        if (pesquisaImovelDto.getAreaTotalMaxima() != null && pesquisaImovelDto.getAreaTotalMaxima() > 0) {
            list.add(cb.lt(root.get("areaTotal").as(Double.class), pesquisaImovelDto.getAreaTotalMaxima()));
        }

        if (pesquisaImovelDto.getAreaTotalMinima() != null && pesquisaImovelDto.getAreaTotalMinima() > 0) {
            list.add(cb.lt(root.get("areaTotal").as(Double.class), pesquisaImovelDto.getAreaTotalMinima()));
        }

        if (pesquisaImovelDto.getValorMinimo() != null && pesquisaImovelDto.getValorMinimo() > 0) {
            list.add(cb.lt(root.get("valor").as(Double.class), pesquisaImovelDto.getValorMinimo()));
        }

        if (pesquisaImovelDto.getValorMaximo() != null && pesquisaImovelDto.getValorMaximo() > 0) {
            list.add(cb.lt(root.get("valor").as(Double.class), pesquisaImovelDto.getValorMaximo()));
        }

        Join<TipoImovel, Imovel> tipoImovelImovelJoin = root.join("tipoImovelByTipoImovelIdTipoImovel");
        if (pesquisaImovelDto.getIdTipo() != null) {
            list.add(tipoImovelImovelJoin.get("idTipoImovel").as(Long.class).in(pesquisaImovelDto.getIdTipo()));
        }

        Join<Construtor, Imovel> construtorImovelJoin = root.join("construtorByConstrutorIdConstrutor");
        if (pesquisaImovelDto.getIdConstrutores() != null) {
            list.add(construtorImovelJoin.get("idConstrutor").as(Long.class).in(pesquisaImovelDto.getIdConstrutores()));
        }

        Join<PerfilImovel, Imovel> perfilImovelImovelJoin = root.join("perfilImovelByPerfilImovelIdPerfilImovel");
        if (pesquisaImovelDto.getIdPerfil() != null) {
            list.add(perfilImovelImovelJoin.get("idPerfilImovel").as(Long.class).in(pesquisaImovelDto.getIdPerfil()));
        }

        Join<ConvenienciaHasImovel, Imovel> convenienciaHasImovelImovelJoin = root.join("convenienciaHasImovelsByIdImovel");
        if (pesquisaImovelDto.getIdConveniencias() != null) {
            list.add(convenienciaHasImovelImovelJoin.get("id").get("convenienciaIdConveniencia").as(Long.class).in(pesquisaImovelDto.getIdConveniencias()));
        }

        Predicate[] p = new Predicate[list.size()];
        return cb.and(list.toArray(p));
    }

}