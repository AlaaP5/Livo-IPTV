package com.rand.ishowApi.clip.admin.domain.specification;
import org.springframework.data.mongodb.core.query.Criteria;
@FunctionalInterface
public interface ClipSpecification {
    Criteria toCriteria();
}
