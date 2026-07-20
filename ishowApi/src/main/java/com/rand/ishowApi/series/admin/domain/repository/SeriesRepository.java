package com.rand.ishowApi.series.admin.domain.repository;

import com.rand.ishowApi.series.admin.domain.entity.Series;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeriesRepository extends MongoRepository<Series, String> {
}

