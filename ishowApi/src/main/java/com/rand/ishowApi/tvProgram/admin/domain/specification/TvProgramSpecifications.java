package com.rand.ishowApi.tvProgram.admin.domain.specification;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.series.admin.domain.specification.SeriesSpecification;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class TvProgramSpecifications {
    public static TvProgramSpecification isActive(Boolean active) {
        return () -> Criteria.where("active").is(active);
    }

    public static TvProgramSpecification isPublish(Boolean isPublish) {
        return () -> Criteria.where("isPublish").is(isPublish);
    }

    public static TvProgramSpecification isKids(Boolean isKids) {
        return () -> Criteria.where("isKids").is(isKids);
    }

    public static TvProgramSpecification isSports(Boolean isSports) {
        return () -> Criteria.where("isSports").is(isSports);
    }

    public static TvProgramSpecification hasRight(Boolean hasRight) {
        return () -> Criteria.where("hasRight").is(hasRight);
    }

    public static TvProgramSpecification hasBadge(Badge badge) {
        return () -> Criteria.where("badge").is(badge);
    }

    public static TvProgramSpecification hasLanguage(ContentLanguage language) {
        return () -> Criteria.where("language").is(language);
    }

    public static TvProgramSpecification hasAnyTagIds(List<Long> tagIds) {
        return () -> Criteria.where("tags.id").in(tagIds);
    }
    public static TvProgramSpecification excludeSeries(String tvProgramId) {
        return () -> Criteria.where("id").ne(tvProgramId);
    }


    public static TvProgramSpecification createdOrUpdatedBy(Long accountId) {
        return () -> new Criteria().orOperator(
                Criteria.where("createdBy").is(accountId),
                Criteria.where("updatedBy").is(accountId)
        );
    }
}

