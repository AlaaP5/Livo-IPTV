package com.rand.ishowApi.tvProgram.admin.domain.repository;

import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramEpisode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TvProgramEpisodeRepository extends MongoRepository<TvProgramEpisode, String> {
    Page<TvProgramEpisode> findPageByTvProgramIdAndSeasonId(String tvProgramId, String seasonId, Pageable pageable);
    Integer countBySeasonIdAndActiveTrueAndIsPublishTrue(String seasonId);

    List<TvProgramEpisode> findBySeasonIdAndActiveTrueAndIsPublishTrueOrderByEpisodeNumberAsc(String seasonId );
}

