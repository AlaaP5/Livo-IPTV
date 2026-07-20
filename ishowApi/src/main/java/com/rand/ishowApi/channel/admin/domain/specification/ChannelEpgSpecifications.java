package com.rand.ishowApi.channel.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;

public class ChannelEpgSpecifications {

    public static ChannelEpgSpecification hasChannelId(String channelId) {
        return () -> Criteria.where("channelId").is(channelId);
    }

    public static ChannelEpgSpecification startDateGte(LocalDateTime start) {
        return () -> Criteria.where("startDate").gte(start);
    }

    public static ChannelEpgSpecification endDateLte(LocalDateTime end) {
        return () -> Criteria.where("endDate").lte(end);
    }

    public static ChannelEpgSpecification isActive(Boolean active) {
        return () -> Criteria.where("active").is(active);
    }
}

