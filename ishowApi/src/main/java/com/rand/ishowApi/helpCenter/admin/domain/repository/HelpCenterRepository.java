package com.rand.ishowApi.helpCenter.admin.domain.repository;

import com.rand.ishowApi.helpCenter.admin.domain.entity.HelpCenter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HelpCenterRepository extends MongoRepository<HelpCenter, String> {}
