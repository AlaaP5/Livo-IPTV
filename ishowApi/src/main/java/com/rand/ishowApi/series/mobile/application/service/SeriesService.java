package com.rand.ishowApi.series.mobile.application.service;


import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.favorite.application.service.FavoriteService;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.openSearch.service.GenericSectionService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.entity.SeriesEpisode;
import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.series.admin.domain.repository.SeriesEpisodeRepository;
import com.rand.ishowApi.series.admin.domain.repository.SeriesRepository;
import com.rand.ishowApi.series.admin.domain.repository.SeriesSeasonRepository;
import com.rand.ishowApi.series.admin.domain.specification.SeriesQueryBuilder;
import com.rand.ishowApi.series.admin.domain.specification.SeriesSpecification;
import com.rand.ishowApi.series.admin.domain.specification.SeriesSpecifications;
import com.rand.ishowApi.series.mobile.api.response.SeasonDetailsResponse;
import com.rand.ishowApi.series.mobile.api.response.SeriesDetailsResponse;
import com.rand.ishowApi.series.mobile.api.response.SeriesSectionResponse;
import com.rand.ishowApi.series.mobile.mapper.SeriesDocMapper;
import com.rand.ishowApi.series.mobile.mapper.SeriesMobileMapper;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.application.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesDocMapper docMapper;
    private final GenericSectionService sectionService;
    private final SeriesRepository repository;
    private final SeriesSeasonRepository seasonRepository;
    private final SeriesMobileMapper mapper;
    private final MongoTemplate mongoTemplate;
    private final SeriesEpisodeRepository episodeRepository;
    private final WatchListService watchListService;
    private final FavoriteService favoriteService;

    public SectionBannerResponse<SeriesSectionResponse> getSeriesSection() throws IOException
    {
        return sectionService.getSections(
                SeriesIndex.SERIES_SECTION.getIndexName(),
                SeriesDocument.class,
                ContentType.SERIES,
                docMapper,
                true

        );
    }
    public List<SeriesSectionResponse> getSeriesSectionById(Long sectionId) throws IOException{
        return sectionService.getSectionContent(
                SeriesIndex.SERIES_SECTION.getIndexName(),
                SeriesDocument.class,
                docMapper,
                sectionId,
                true
        )   ;
    }
    public SeriesDetailsResponse getSeriesDetails(String seriesId){

       Series series= repository.findById(seriesId)
               .orElseThrow(()->  new CustomException(ApiResponseCode.SERIES_NOT_FOUND,null));
       List<SeriesSeason>  seasons=seasonRepository.
               findBySeriesIdAndActiveTrueOrderBySeasonNumberAsc(series.getId());


        List<Long> tagIds= series.getTags()
                .stream()
                .map(TagMeta::getId)
                .toList();

        List<SeriesSpecification> specs = List.of(
                SeriesSpecifications.isActive(true),
                SeriesSpecifications.isPublish(true),
                SeriesSpecifications.hasLanguage(series.getLanguage()),
                SeriesSpecifications.isKids(series.getIsKids()),
                SeriesSpecifications.isSports(series.getIsSports()),
                SeriesSpecifications.hasAnyTagIds(tagIds),
                SeriesSpecifications.excludeSeries(seriesId)
        );

        Query query = SeriesQueryBuilder.build(specs);
        query.with(Sort.by(Sort.Direction.DESC, "rating"));
        query.limit(5);

       List<Series> relatedSeries= mongoTemplate.find(query, Series.class);

        boolean isLke = favoriteService.isInFavorite(seriesId, ContentType.SERIES);
        boolean isWatchList = watchListService.isInWatchList(seriesId, ContentType.SERIES);

     return mapper.toSeriesDetailsResponse(series, seasons, relatedSeries,  isLke, isWatchList);

    }
    public SeasonDetailsResponse getSeasonDetail(String seasonId) {
        SeriesSeason season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_SEASON_NOT_FOUND, null));
        List<SeriesEpisode> episodes = episodeRepository.
                findBySeasonIdAndActiveTrueAndIsPublishTrueOrderByEpisodeNumberAsc(seasonId);

        return mapper.mapSeasonDetails(season, episodes);
    }


}
