package com.rand.ishowApi.series.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;

public interface SeriesSpecification {
    Criteria toCriteria();
}

