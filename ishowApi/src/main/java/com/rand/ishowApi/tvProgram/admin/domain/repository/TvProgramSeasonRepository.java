package com.rand.ishowApi.tvProgram.admin.domain.repository;

import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TvProgramSeasonRepository extends MongoRepository<TvProgramSeason, String> {
    List<TvProgramSeason> findByTvProgramId(String tvProgramId);
    Integer countByTvProgramIdAndActiveTrue(String tvProgramId);
    List<TvProgramSeason> findByTvProgramIdAndActiveTrueOrderBySeasonNumberAsc(String tvProgramId);


}

