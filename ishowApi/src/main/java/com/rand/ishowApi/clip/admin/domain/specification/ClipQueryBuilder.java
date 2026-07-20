package com.rand.ishowApi.clip.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
public class ClipQueryBuilder {
    public static Query build(List<ClipSpecification> specs) {
        List<Criteria> criteriaList = specs.stream()
                .map(ClipSpecification::toCriteria)
                .toList();
        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
        return query;
    }
}
