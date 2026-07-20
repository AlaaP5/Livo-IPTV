package com.rand.ishowApi.section.domain.repository;

import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SectionSpecification {

    public static Specification<Section> hasActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<Section> hasPublish(Boolean publish) {
        return (root, query, cb) -> {
            if (publish == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("publish"), publish);
        };
    }

    public static Specification<Section> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isBlank()) {
                return cb.conjunction();
            }

            String searchPattern = "%" + title.toLowerCase() + "%";

            Predicate titleEnPredicate = cb.like(cb.lower(root.get("titleEn")), searchPattern);
            Predicate titleArPredicate = cb.like(cb.lower(root.get("titleAr")), searchPattern);
            return cb.or(titleEnPredicate, titleArPredicate);
        };
    }

    public static Specification<Section> hasPage(MobilePage page) {
        return (root, query, cb) -> {
            if (page == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("page"), page);
        };
    }

    public static Specification<Section> hasContentType(ContentType contentType) {
        return (root, query, cb) -> {
            if (contentType == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("contentType"), contentType);
        };
    }
}

