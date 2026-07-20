package com.rand.ishowApi.series.admin.domain.specification;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.movie.admin.domain.specification.MovieSpecification;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class SeriesSpecifications {
    public static SeriesSpecification isActive(Boolean active) {
        return () -> Criteria.where("active").is(active);
    }

    public static SeriesSpecification isPublish(Boolean isPublish) {
        return () -> Criteria.where("isPublish").is(isPublish);
    }

    public static SeriesSpecification isKids(Boolean isKids) {
        return () -> Criteria.where("isKids").is(isKids);
    }

    public static SeriesSpecification isSports(Boolean isSports) {
        return () -> Criteria.where("isSports").is(isSports);
    }

    public static SeriesSpecification hasRight(Boolean hasRight) {
        return () -> Criteria.where("hasRight").is(hasRight);
    }

    public static SeriesSpecification hasBadge(Badge badge) {
        return () -> Criteria.where("badge").is(badge);
    }

    public static SeriesSpecification hasLanguage(ContentLanguage language) {
        return () -> Criteria.where("language").is(language);
    }

    public static SeriesSpecification hasAnyTagIds(List<Long> tagIds) {
        return () -> Criteria.where("tags.id").in(tagIds);
    }
    public static SeriesSpecification excludeSeries(String seriesId) {
        return () -> Criteria.where("id").ne(seriesId);
    }


    public static SeriesSpecification createdOrUpdatedBy(Long accountId) {
        return () -> new Criteria().orOperator(
                Criteria.where("createdBy").is(accountId),
                Criteria.where("updatedBy").is(accountId)
        );
    }
}

