package com.rand.ishowApi.series.admin.application.service;

import com.rand.ishowApi.actor.domain.entity.Actor;
import com.rand.ishowApi.actor.domain.repository.ActorRepository;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.mapper.MetaHelper;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
import com.rand.ishowApi.series.admin.api.request.AddSeriesAdminRequest;
import com.rand.ishowApi.series.admin.api.request.FilterSeriesAdminRequest;
import com.rand.ishowApi.series.admin.api.request.UpdateSeriesAdminRequest;
import com.rand.ishowApi.series.admin.api.response.SeriesAdminResponse;
import com.rand.ishowApi.series.admin.api.response.SeriesSectionAdminResponse;
import com.rand.ishowApi.series.admin.application.manager.SeriesDomainManager;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.series.admin.domain.repository.SeriesRepository;
import com.rand.ishowApi.series.admin.domain.specification.SeriesQueryBuilder;
import com.rand.ishowApi.series.admin.domain.specification.SeriesSpecification;
import com.rand.ishowApi.series.admin.domain.specification.SeriesSpecifications;
import com.rand.ishowApi.series.admin.mapper.SeriesAdminMapper;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.admin.domain.repository.TagRepository;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SeriesAdminService {
    private final SeriesRepository seriesRepository;
    private final TagRepository tagRepository;
    private final SeriesDomainManager seriesDomainManager;
    private final SeriesAdminMapper seriesAdminMapper;
    private final UploadServiceAsync uploadServiceAsync;
    private final MongoTemplate mongoTemplate;
    private final MetaHelper metaHelper;
    private final ActorRepository actorRepository;
    private final SeriesOpenSearchService seriesOpenSearchService;
    private final SectionRepository sectionRepository;

    // ================================================
    // =========== Series
    // =================================================

    public Series addSeries(AddSeriesAdminRequest request) {
        List<Tag> tagList = request.getTags() != null
                ? tagRepository.findAllById(request.getTags())
                : null;
        List<TagMeta>tagMetaList =metaHelper.mapMetaTag(tagList);

        List<ActorMeta> actorMetaList = null;
        if (request.getActors() != null) {
            List<Actor> actors = actorRepository.findAllById(request.getActors());
            actorMetaList = metaHelper.mapMetaActor(actors);
        }

        Series series = seriesDomainManager.create(request, tagMetaList, actorMetaList);
        seriesRepository.save(series);

        CompletableFuture<OriginalUploadMetadata> posterFuture =
                uploadServiceAsync.uploadSeriesPosterAsync(request.getPoster(), series.getId());

        CompletableFuture.allOf(posterFuture).join();
        OriginalUploadMetadata poster = posterFuture.join();

        seriesDomainManager.addSeriesPoster(series, poster);
        return seriesRepository.save(series);
    }

    public Series findSeriesById(String id) {
        return findSeriesEntityById(id);
    }

    private Series findSeriesEntityById(String id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_NOT_FOUND, null));
    }

    public Series updateSeries(UpdateSeriesAdminRequest request) throws IOException {
        Series series = findSeriesEntityById(request.getSeriesId());

        List<TagMeta> tagMetaList = null;
        if (request.getTags() != null) {
            List<Tag> tags = tagRepository.findAllById(request.getTags());
            tagMetaList =metaHelper.mapMetaTag(tags);
        }

        List<ActorMeta> actorMetaList = null;
        if (request.getActors() != null) {
            List<Actor> actors = actorRepository.findAllById(request.getActors());
            actorMetaList = metaHelper.mapMetaActor(actors);
        }

        CompletableFuture<OriginalUploadMetadata> posterFuture = null;
        if (request.getPoster() != null) {
            posterFuture = uploadServiceAsync.uploadSeriesPosterAsync(request.getPoster(), series.getId());
            CompletableFuture.allOf(posterFuture).join();
        }

        OriginalUploadMetadata posterMetadata = posterFuture != null ? posterFuture.join() : null;

        if (posterMetadata != null || request.getPoster() != null) {
            if (series.getPoster() != null) {
                uploadServiceAsync.removeOldFileAsync(series.getPoster());
            }
        }

        seriesDomainManager.update(series, request, tagMetaList,actorMetaList, posterMetadata);

        if(series.getIsPublish()) {
            seriesOpenSearchService.updateSeriesIndexes (series);
        }
        return seriesRepository.save(series);
    }

    public Page<SeriesAdminResponse> filter(FilterSeriesAdminRequest request) {
        List<SeriesSpecification> specs = new ArrayList<>();

        if (request.getBadge() != null) {
            specs.add(SeriesSpecifications.hasBadge(request.getBadge()));
        }

        if (request.getLanguage() != null) {
            specs.add(SeriesSpecifications.hasLanguage(request.getLanguage()));
        }

        if (request.getAccountId() != null) {
            specs.add(SeriesSpecifications.createdOrUpdatedBy(request.getAccountId()));
        }

        if (request.getIsActive() != null) {
            specs.add(SeriesSpecifications.isActive(BooleanConverter.getActiveBoolean(request.getIsActive()) ));
        }

        if (request.getIsPublish() != null) {
            specs.add(SeriesSpecifications.isPublish(BooleanConverter.getActiveBoolean(request.getIsPublish())));
        }

        if (request.getIsKids() != null) {
            specs.add(SeriesSpecifications.isKids(BooleanConverter.getActiveBoolean(request.getIsKids())));
        }

        if (request.getIsSport() != null) {
            specs.add(SeriesSpecifications.isSports(BooleanConverter.getActiveBoolean(request.getIsSport())));
        }

        if (request.getHasRight() != null) {
            specs.add(SeriesSpecifications.hasRight(BooleanConverter.getActiveBoolean(request.getHasRight())));
        }

        Query query = SeriesQueryBuilder.build(specs);

        if (request.getName() != null && !request.getName().isEmpty()) {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                    .matching(request.getName());
            query.addCriteria(textCriteria);
        }

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        long total = mongoTemplate.count(query, Series.class);
        query.with(pageable);

        List<Series> seriesList = mongoTemplate.find(query, Series.class);

        return new PageImpl<>(
                seriesAdminMapper.toFilterResponse(seriesList),
                pageable,
                total
        );
    }



    public Series publishSeries(String  seriesId){
        Series series =findSeriesEntityById(seriesId);

        series.setIsPublish(true);
        seriesOpenSearchService.updateSeriesSearchIndex(series);
        return seriesRepository.save(series);
    }


    public void addSeriesToSection(String seriesId, Long sectionId, String isTop) throws IOException {
        Series series = findSeriesEntityById(seriesId);
        Section section=sectionRepository.findById(sectionId)
                .orElseThrow( ()->  new CustomException(ApiResponseCode.SECTION_NOT_EXISTS ,null));

        seriesOpenSearchService.addSeriesToSection(series,section,SeriesIndex.SERIES_SECTION.getIndexName(),isTop);
    }

    public void removeSeriesFromSection(Long sectionId, String seriesId) throws IOException {
        seriesOpenSearchService.removeSeriesFromSection(sectionId,seriesId,SeriesIndex.SERIES_SECTION.getIndexName());

    }

    public List<SeriesSectionAdminResponse> getSeriesSection(Long sectionId, String isTop, int page, int size) throws IOException {
     return    seriesOpenSearchService.getSeriesSection(sectionId,SeriesIndex.SERIES_SECTION.getIndexName(),isTop,page,size);
    }

    public void updateSeriesIsTop(Long sectionId, String seriesId, String isTop) throws IOException {
        seriesOpenSearchService.updateSeriesIsTop(sectionId,seriesId,SeriesIndex.SERIES_SECTION.getIndexName(),  isTop);
    }
}

