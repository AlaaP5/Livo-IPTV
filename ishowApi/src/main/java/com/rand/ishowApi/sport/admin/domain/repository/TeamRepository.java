package com.rand.ishowApi.sport.admin.domain.repository;

import com.rand.ishowApi.sport.admin.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TeamRepository extends JpaRepository<Team,Long>, JpaSpecificationExecutor<Team> {

    boolean existsByNameEnAndNameArAndActive(String nameEn, String nameAr, Boolean active);
}

