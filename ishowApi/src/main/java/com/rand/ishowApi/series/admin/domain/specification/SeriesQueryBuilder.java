package com.rand.ishowApi.series.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class SeriesQueryBuilder {
    public static Query build(List<SeriesSpecification> specs) {
        List<Criteria> criteriaList = specs.stream()
                .map(SeriesSpecification::toCriteria)
                .toList();

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return query;
    }
}

