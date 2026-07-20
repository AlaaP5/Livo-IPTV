package com.rand.ishowApi.channel.admin.domain.repository;

import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelRepository extends MongoRepository<Channel,String> {
}
