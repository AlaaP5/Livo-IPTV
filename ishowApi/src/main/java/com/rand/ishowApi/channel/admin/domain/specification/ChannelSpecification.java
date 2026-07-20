package com.rand.ishowApi.channel.admin.domain.specification;

import org.springframework.data.mongodb.core.query.Criteria;

public interface ChannelSpecification {

    Criteria toCriteria();
}
