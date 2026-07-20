package com.rand.ishowApi.sport.admin.domain.repository;

import com.rand.ishowApi.sport.admin.domain.entity.UpcomingMatch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UpcomingMatchRepository extends MongoRepository<UpcomingMatch, String> {
    List<UpcomingMatch> findByMatchDateGreaterThanEqualAndActiveTrue(LocalDateTime dateTime);
}

