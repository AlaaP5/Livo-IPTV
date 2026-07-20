package com.rand.ishowApi.tvProgram.mobile.application.service;


import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.favorite.application.service.FavoriteService;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.openSearch.service.GenericSectionService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramEpisode;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.index.TvProgramIndex;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramEpisodeRepository;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramRepository;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramSeasonRepository;
import com.rand.ishowApi.tvProgram.admin.domain.specification.TvProgramQueryBuilder;
import com.rand.ishowApi.tvProgram.admin.domain.specification.TvProgramSpecification;
import com.rand.ishowApi.tvProgram.admin.domain.specification.TvProgramSpecifications;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramDetailsResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSectionResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSeasonDetailsResponse;
import com.rand.ishowApi.tvProgram.mobile.mapper.TvProgramDocMapper;
import com.rand.ishowApi.tvProgram.mobile.mapper.TvProgramMobileMapper;
import com.rand.ishowApi.watchList.application.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TvProgramMobileService {

    private final TvProgramDocMapper docMapper;
    private final GenericSectionService sectionService;
    private final TvProgramRepository repository;
    private final TvProgramSeasonRepository seasonRepository ;
    private final TvProgramEpisodeRepository episodeRepository;
    private final MongoTemplate mongoTemplate;
    private final TvProgramMobileMapper mapper;
    private final WatchListService watchListService;
    private final FavoriteService favoriteService;

    public SectionBannerResponse<TvProgramSectionResponse> getTvProgramSection() throws IOException
    {
        return sectionService.getSections(
                TvProgramIndex.TV_PROGRAM_SECTION.getIndexName(),
                TvProgramDocument.class,
                ContentType.TV_PROGRAMS,
                docMapper,
                true

        );
    }
    public List<TvProgramSectionResponse> getTvProgramSectionById(Long sectionId) throws IOException{
        return sectionService.getSectionContent(
                TvProgramIndex.TV_PROGRAM_SECTION.getIndexName(),
                TvProgramDocument.class,
                docMapper,
                sectionId,
                true
        )   ;
    }
    public TvProgramDetailsResponse getTvProgramDetails(String tvProgramId){

        TvProgram tvProgram= repository.findById(tvProgramId)
                .orElseThrow(()->  new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND,null));
        List<TvProgramSeason>  seasons=seasonRepository.
                findByTvProgramIdAndActiveTrueOrderBySeasonNumberAsc(tvProgram.getId());


        List<Long> tagIds= tvProgram.getTags()
                .stream()
                .map(TagMeta::getId)
                .toList();

        List<TvProgramSpecification> specs = List.of(
                TvProgramSpecifications.isActive(true),
                TvProgramSpecifications.isPublish(true),
                TvProgramSpecifications.hasLanguage(tvProgram.getLanguage()),
                TvProgramSpecifications.isKids(tvProgram.getIsKids()),
                TvProgramSpecifications.isSports(tvProgram.getIsSports()),
                TvProgramSpecifications.hasAnyTagIds(tagIds),
                TvProgramSpecifications.excludeSeries(tvProgram.getId())
        );

        Query query = TvProgramQueryBuilder.build(specs);
        query.limit(5);

        List<TvProgram> relatedProgram= mongoTemplate.find(query, TvProgram.class);

        boolean isLke = favoriteService.isInFavorite(tvProgramId, ContentType.TV_PROGRAMS);
        boolean isWatchList = watchListService.isInWatchList(tvProgramId, ContentType.TV_PROGRAMS);

        return mapper.toTvProgramDetailsResponse(tvProgram, seasons, relatedProgram,  isLke, isWatchList);

    }
    public TvProgramSeasonDetailsResponse  getSeasonDetail(String seasonId) {
        TvProgramSeason season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_SEASON_NOT_FOUND, null));

        List<TvProgramEpisode> episodes = episodeRepository.
                findBySeasonIdAndActiveTrueAndIsPublishTrueOrderByEpisodeNumberAsc(seasonId);

        return mapper.mapSeasonDetails(season, episodes);
    }



}
