package com.rand.ishowApi.actor.domain.repository;

import com.rand.ishowApi.actor.domain.entity.Actor;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class ActorSpecification {

    public static Specification<Actor> hasActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<Actor> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction();
            }

            String searchPattern = "%" + name.toLowerCase() + "%";

            Predicate nameEnPredicate = cb.like(
                    cb.lower(root.get("nameEn")),
                    searchPattern
            );

            Predicate nameArPredicate = cb.like(
                    cb.lower(root.get("nameAr")),
                    searchPattern
            );
            return cb.or(nameEnPredicate, nameArPredicate);
        };
    }
}
