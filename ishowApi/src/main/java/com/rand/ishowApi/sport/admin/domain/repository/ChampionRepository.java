package com.rand.ishowApi.sport.admin.domain.repository;

import com.rand.ishowApi.sport.admin.domain.entity.Champion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ChampionRepository extends JpaRepository<Champion,Long>, JpaSpecificationExecutor<Champion> {
    boolean existsByNameEnAndNameArAndActive(String nameEn, String nameAr, Boolean active);
}