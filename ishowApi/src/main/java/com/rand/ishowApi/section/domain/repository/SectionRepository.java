package com.rand.ishowApi.section.domain.repository;

import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long>, JpaSpecificationExecutor<Section> {

    @Query("""
            select case when count(s) > 0 then true else false end
            from Section s
            where (lower(s.titleEn) = lower(:titleEn)
               or lower(s.titleAr) = lower(:titleAr))
              and s.page = :page
              and s.contentType = :contentType
            """)
    boolean existsByTitleEnIgnoreCaseOrTitleArIgnoreCase(@Param("titleEn") String titleEn,
                                                         @Param("titleAr") String titleAr,
                                                         @Param("page") MobilePage page,
                                                         @Param("contentType") ContentType contentType);

    @Query("""
            select case when count(s) > 0 then true else false end
            from Section s
            where s.id <> :id
              and (lower(s.titleEn) = lower(:titleEn) or lower(s.titleAr) = lower(:titleAr))
              and s.page = :page
              and s.contentType = :contentType
            """)
    boolean existsByIdNotAndTitleEnIgnoreCaseOrTitleArIgnoreCase(@Param("id") Long id,
                                                                 @Param("titleEn") String titleEn,
                                                                 @Param("titleAr") String titleAr,
                                                                 @Param("page") MobilePage page,
                                                                 @Param("contentType") ContentType contentType);
}

