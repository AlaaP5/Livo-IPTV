package com.rand.ishowApi.ads.domain.repository;

import com.rand.ishowApi.ads.domain.entity.Ad;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AdSpecification {

    public static Specification<Ad> hasActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("active"), active);
        };
    }

    public static Specification<Ad> hasBetweenDates(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return (root, query, cb) -> {

            if (startDate != null && endDate != null) {
                return cb.and(
                        cb.equal(root.get("startDate"), startDate),
                        cb.equal(root.get("endDate"), endDate)
                );
            }

            if (startDate != null) {
                return cb.equal(root.get("startDate"), startDate);
            }

            if (endDate != null) {
                return cb.equal(root.get("endDate"), endDate);
            }

            return cb.conjunction();
        };
    }
}
