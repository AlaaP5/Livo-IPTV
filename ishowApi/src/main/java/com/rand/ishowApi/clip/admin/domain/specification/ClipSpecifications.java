package com.rand.ishowApi.clip.admin.domain.specification;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TranscodeStatus;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class ClipSpecifications {
    public static ClipSpecification hasTranscodeStatus(TranscodeStatus status) {
        return () -> Criteria.where("transcodeStatus").is(status);
    }
    public static ClipSpecification isActive(Boolean active) {
        return () -> Criteria.where("active").is(active);
    }
    public static ClipSpecification isPublish(Boolean isPublish) {
        return () -> Criteria.where("isPublish").is(isPublish);
    }
    public static ClipSpecification isKids(Boolean isKids) {
        return () -> Criteria.where("isKids").is(isKids);
    }
    public static ClipSpecification isSports(Boolean isSports) {
        return () -> Criteria.where("isSports").is(isSports);
    }
    public static ClipSpecification hasRight(Boolean hasRight) {
        return () -> Criteria.where("hasRight").is(hasRight);
    }
    public static ClipSpecification hasAccessType(AccessType accessType) {
        return () -> Criteria.where("accessType").is(accessType);
    }

    public static ClipSpecification excludeClip(String clipId) {
        return () -> Criteria.where("id").ne(clipId);
    }

    public static ClipSpecification hasAnyTagIds(List<Long> tagIds) {
        return () -> Criteria.where("tags.id").in(tagIds);
    }
    public static ClipSpecification hasBadge(Badge badge) {
        return () -> Criteria.where("badge").is(badge);
    }
    public static ClipSpecification createdOrUpdatedBy(Long accountId) {
        return () -> new Criteria().orOperator(
                Criteria.where("createdBy").is(accountId),
                Criteria.where("updatedBy").is(accountId)
        );
    }
}
