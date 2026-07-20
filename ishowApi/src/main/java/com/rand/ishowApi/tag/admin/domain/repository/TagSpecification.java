package com.rand.ishowApi.tag.admin.domain.repository;

import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TagSpecification {

    public static Specification<Tag> hasActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<Tag> hasCommon(Boolean common) {
        return (root, query, cb) -> {
            if (common == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("common"), common);
        };
    }

    public static Specification<Tag> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isBlank()) {
                return cb.conjunction();
            }

            String searchPattern = "%" + title.toLowerCase() + "%";

            Predicate titleEnPredicate = cb.like(
                    cb.lower(root.get("titleEn")),
                    searchPattern
            );

            Predicate titleArPredicate = cb.like(
                    cb.lower(root.get("titleAr")),
                    searchPattern
            );
            return cb.or(titleEnPredicate, titleArPredicate);
        };
    }



}
