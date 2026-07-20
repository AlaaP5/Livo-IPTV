package com.rand.ishowApi.movie.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;

public interface MovieSpecification {
    Criteria toCriteria();

}
