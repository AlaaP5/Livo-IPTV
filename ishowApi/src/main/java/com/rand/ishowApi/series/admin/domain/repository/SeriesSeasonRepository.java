package com.rand.ishowApi.series.admin.domain.repository;

import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SeriesSeasonRepository extends MongoRepository<SeriesSeason, String> {

    List<SeriesSeason> findBySeriesId(String seriesId);

    Integer countBySeriesIdAndActiveTrue(String seriesId);

    List<SeriesSeason> findBySeriesIdAndActiveTrueOrderBySeasonNumberAsc( String seriesId);


}
