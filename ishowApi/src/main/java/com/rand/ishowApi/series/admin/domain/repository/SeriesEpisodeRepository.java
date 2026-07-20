package com.rand.ishowApi.series.admin.domain.repository;

import com.rand.ishowApi.series.admin.domain.entity.SeriesEpisode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SeriesEpisodeRepository  extends MongoRepository<SeriesEpisode ,String> {

    Page<SeriesEpisode> findPageBySeriesIdAndSeasonId(String seriesId, String seasonId, Pageable pageable);

    Integer countBySeasonIdAndActiveTrueAndIsPublishTrue(String seasonId);

    List<SeriesEpisode> findBySeasonIdAndActiveTrueAndIsPublishTrueOrderByEpisodeNumberAsc(String seasonId);
}

