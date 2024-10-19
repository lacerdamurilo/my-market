package br.edu.ifrs.restinga.market.mymarket.repository.spec;

import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
public class ProductSpecification implements Specification<Product> {

    @Builder.Default
    private final transient Optional<String> name = Optional.empty();
    @Builder.Default
    private final transient Optional<String> type = Optional.empty();

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        name.ifPresent(s -> predicates.add(builder.like(builder.lower(root.get("name")), "%" + s.toLowerCase() + "%")));
        type.ifPresent(s -> predicates.add(builder.like(builder.lower(root.get("type")), "%" + s.toLowerCase() + "%")));

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
