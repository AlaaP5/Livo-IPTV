package com.rand.ishowApi.clip.admin.domain.repository;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ClipRepository extends MongoRepository<Clip, String> {
}
