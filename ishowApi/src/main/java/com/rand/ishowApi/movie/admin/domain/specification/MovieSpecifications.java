package com.rand.ishowApi.movie.admin.domain.specification;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TranscodeStatus;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class MovieSpecifications {
    public static MovieSpecification hasTranscodeStatus(TranscodeStatus status) {
        return () -> Criteria.where("transcodeStatus").is(status);
    }

    public static MovieSpecification isActive(Boolean active) {
        return () -> Criteria.where("active").is(active);
    }

    public static MovieSpecification isPublish(Boolean isPublish) {
        return () -> Criteria.where("isPublish").is(isPublish);
    }

    public static MovieSpecification isKids(Boolean isKids) {
        return () -> Criteria.where("isKids").is(isKids);
    }

    public static MovieSpecification isSports(Boolean isSports) {
        return () -> Criteria.where("isSports").is(isSports);
    }

    public static MovieSpecification hasAccessType(AccessType accessType) {
        return () -> Criteria.where("accessType").is(accessType);
    }

    public static MovieSpecification hasBadge(Badge badge) {
        return () -> Criteria.where("badge").is(badge);
    }

    public static MovieSpecification hasLanguage(ContentLanguage language) {
        return () -> Criteria.where("language").is(language);
    }



    public static MovieSpecification hasActor(Long actorId) {
        return () -> Criteria.where("actors.id").is(actorId);
    }

    public static MovieSpecification hasAnyTagIds(List<Long> tagIds) {
        return () -> Criteria.where("tags.id").in(tagIds);
    }
    public static MovieSpecification excludeMovie(String movieId) {
        return () -> Criteria.where("id").ne(movieId);
    }

    // 🔥 accountId → match createdBy OR updatedBy
    public static MovieSpecification createdOrUpdatedBy(Long accountId) {
        return () -> new Criteria().orOperator(
                Criteria.where("createdBy").is(accountId),
                Criteria.where("updatedBy").is(accountId)
        );
    }
}
