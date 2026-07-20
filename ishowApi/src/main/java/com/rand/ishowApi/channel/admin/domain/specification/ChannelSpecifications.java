package com.rand.ishowApi.channel.admin.domain.specification;

import com.rand.ishowApi.metadata.Badge;
import org.springframework.data.mongodb.core.query.Criteria;

public class ChannelSpecifications {

//    public static ChannelSpecification hasTranscodeStatus(TranscodeStatus status) {
//        return () -> Criteria.where("transcodeStatus").is(status);
//    }

    public static ChannelSpecification isActive(Boolean active) {
        return () -> Criteria.where("active").is(active);
    }

    public static ChannelSpecification isPublish(Boolean isPublish) {
        return () -> Criteria.where("isPublish").is(isPublish);
    }

    public static ChannelSpecification isKids(Boolean isKids) {
        return () -> Criteria.where("isKids").is(isKids);
    }

    public static ChannelSpecification isSports(Boolean isSports) {
        return () -> Criteria.where("isSports").is(isSports);
    }

//    public static ChannelSpecification hasAccessType(AccessType accessType) {
//        return () -> Criteria.where("accessType").is(accessType);
//    }

    public static ChannelSpecification hasBadge(Badge badge) {
        return () -> Criteria.where("badge").is(badge);
    }

//    public static ChannelSpecification hasLanguage(ContentLanguage language) {
//        return () -> Criteria.where("language").is(language);
//    }

    public static ChannelSpecification hasTag(Long tagId) {
        return () -> Criteria.where("tags.id").is(tagId);
    }

    // 🔥 accountId → match createdBy OR updatedBy
    public static ChannelSpecification createdOrUpdatedBy(Long accountId) {
        return () -> new Criteria().orOperator(
                Criteria.where("createdBy").is(accountId),
                Criteria.where("updatedBy").is(accountId)
        );
    }
}
