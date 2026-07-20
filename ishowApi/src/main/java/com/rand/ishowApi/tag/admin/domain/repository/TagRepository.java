package com.rand.ishowApi.tag.admin.domain.repository;

import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

	boolean existsByTitleEnIgnoreCaseOrTitleArIgnoreCase(String titleEn, String titleAr);

	boolean existsByIdNotAndTitleEnIgnoreCaseOrIdNotAndTitleArIgnoreCase(
			Long id,
			String titleEn,
			Long sameId,
			String titleAr
	);

    java.util.List<Tag> findAllByCommonTrueAndActiveTrueOrderByIdAsc();
}
