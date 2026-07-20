package com.rand.ishowApi.sport.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;

public interface UpcomingMatchSpecification {
    Criteria toCriteria();
}

