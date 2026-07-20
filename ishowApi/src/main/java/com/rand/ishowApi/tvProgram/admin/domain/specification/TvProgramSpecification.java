package com.rand.ishowApi.tvProgram.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;

public interface TvProgramSpecification {
    Criteria toCriteria();
}
