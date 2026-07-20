package com.rand.ishowApi.actor.domain.repository;

import com.rand.ishowApi.actor.domain.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ActorRepository extends JpaRepository<Actor,Long>, JpaSpecificationExecutor<Actor> {

    boolean existsByNameEnAndNameArAndActive(String nameEn, String nameAr, Boolean active);
}
