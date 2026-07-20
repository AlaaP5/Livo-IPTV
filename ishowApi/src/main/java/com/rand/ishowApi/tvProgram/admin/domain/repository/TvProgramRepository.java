package com.rand.ishowApi.tvProgram.admin.domain.repository;

import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TvProgramRepository extends MongoRepository<TvProgram, String> {

}



