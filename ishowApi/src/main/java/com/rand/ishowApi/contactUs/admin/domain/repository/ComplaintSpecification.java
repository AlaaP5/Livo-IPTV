package com.rand.ishowApi.contactUs.admin.domain.repository;

import com.rand.ishowApi.contactUs.admin.domain.entity.Complaint;
import org.springframework.data.jpa.domain.Specification;

public class ComplaintSpecification {

    public static Specification<Complaint> isRead(Boolean status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isRead"), status);
        };
    }

    public static Specification<Complaint> alternativePhoneEquals(String alternativePhone) {
        return (root, query, cb) -> {
            if (alternativePhone == null || alternativePhone.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("alternativePhone"), alternativePhone);
        };
    }

    public static Specification<Complaint> titleContains(String title) {
        return (root, query, cb) -> {
            if (title == null || title.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }
}
