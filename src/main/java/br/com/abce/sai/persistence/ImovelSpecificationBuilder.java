package br.com.abce.sai.persistence;

import br.com.abce.sai.dto.SearchCriteriaDto;
import br.com.abce.sai.dto.SearchOperation;
import br.com.abce.sai.persistence.model.Imovel;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImovelSpecificationBuilder {

    private final List<SearchCriteriaDto> params;

    public ImovelSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public ImovelSpecificationBuilder with(String predicate, String key, String operation, Object value) {
        boolean orPredicate = predicate != null && predicate.equals(SearchOperation.OR_PREDICATE_FLAG);
        params.add(new SearchCriteriaDto(key, operation, value, orPredicate));
        return this;
    }

    public Specification<Imovel> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map((SearchCriteriaDto t) -> new ImovelSpecification(t))
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Specification.where(result)
                    .or(specs.get(i))
                    : Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }

}
