package com.rand.ishowApi.movie.admin.domain.repository;

import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MoviesRepository extends MongoRepository<Movies,String> {
}
