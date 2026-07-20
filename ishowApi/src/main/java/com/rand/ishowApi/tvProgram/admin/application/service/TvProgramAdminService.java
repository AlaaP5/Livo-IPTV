package com.rand.ishowApi.tvProgram.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.mapper.MetaHelper;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.admin.domain.repository.TagRepository;
import com.rand.ishowApi.tvProgram.admin.api.request.AddTvProgramAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.request.FilterTvProgramAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.request.UpdateTvProgramAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramAdminResponse;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramSectionAdminResponse;
import com.rand.ishowApi.tvProgram.admin.application.manager.TvProgramDomainManager;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.index.TvProgramIndex;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramRepository;
import com.rand.ishowApi.tvProgram.admin.domain.specification.TvProgramQueryBuilder;
import com.rand.ishowApi.tvProgram.admin.domain.specification.TvProgramSpecification;
import com.rand.ishowApi.tvProgram.admin.domain.specification.TvProgramSpecifications;
import com.rand.ishowApi.tvProgram.admin.mapper.TvProgramAdminMapper;
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
public class TvProgramAdminService {
    private final TvProgramRepository tvProgramRepository;
    private final TagRepository tagRepository;
    private final TvProgramDomainManager tvProgramDomainManager;
    private final TvProgramAdminMapper tvProgramAdminMapper;
    private final UploadServiceAsync uploadServiceAsync;
    private final MongoTemplate mongoTemplate;
    private final MetaHelper metaHelper;
    private final TvProgramOpenSearchService tvProgramOpenSearchService;
    private final SectionRepository sectionRepository;

    // ================================================
    // =========== TvProgram
    // =================================================

    public TvProgram addTvProgram(AddTvProgramAdminRequest request) {
        List<Tag> tagList = request.getTags() != null
                ? tagRepository.findAllById(request.getTags())
                : null;

        List<TagMeta>tagMetaList =metaHelper.mapMetaTag(tagList);
        TvProgram tvProgram = tvProgramDomainManager.create(request, tagMetaList);
        tvProgramRepository.save(tvProgram);

        CompletableFuture<OriginalUploadMetadata> posterFuture =
                uploadServiceAsync.uploadTvProgramPosterAsync(request.getPoster(), tvProgram.getId());

        CompletableFuture.allOf(posterFuture).join();
        OriginalUploadMetadata poster = posterFuture.join();

        tvProgramDomainManager.addTvProgramPoster(tvProgram, poster);
        return tvProgramRepository.save(tvProgram);
    }
    public TvProgram findTvProgramById(String id) {
        return findTvProgramEntityById(id);
    }
    private TvProgram findTvProgramEntityById(String id) {
        return tvProgramRepository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND, null));
    }
    public TvProgram updateTvProgram(UpdateTvProgramAdminRequest request) throws IOException {
        TvProgram tvProgram = findTvProgramEntityById(request.getTvProgramId());

        List<TagMeta> tagMetaList = null;
        if (request.getTags() != null) {
            List<Tag> tags = tagRepository.findAllById(request.getTags());
            tagMetaList = metaHelper.mapMetaTag(tags);
        }
        CompletableFuture<OriginalUploadMetadata> posterFuture = null;
        if (request.getPoster() != null) {
            posterFuture = uploadServiceAsync.uploadTvProgramPosterAsync(request.getPoster(), tvProgram.getId());
            CompletableFuture.allOf(posterFuture).join();
        }

        OriginalUploadMetadata posterMetadata = posterFuture != null ? posterFuture.join() : null;

        if (posterMetadata != null || request.getPoster() != null) {
            if (tvProgram.getPoster() != null) {
                uploadServiceAsync.removeOldFileAsync(tvProgram.getPoster());
            }
        }

        tvProgramDomainManager.update(tvProgram, request, tagMetaList, posterMetadata);

        if (tvProgram.getIsPublish()) {
            tvProgramOpenSearchService.updateTvProgramIndexes(tvProgram);
        }

        return tvProgramRepository.save(tvProgram);
    }
    public Page<TvProgramAdminResponse> filter(FilterTvProgramAdminRequest request) {
        List<TvProgramSpecification> specs = new ArrayList<>();

        if (request.getBadge() != null) {
            specs.add(TvProgramSpecifications.hasBadge(request.getBadge()));
        }

        if (request.getLanguage() != null) {
            specs.add(TvProgramSpecifications.hasLanguage(request.getLanguage()));
        }

        if (request.getAccountId() != null) {
            specs.add(TvProgramSpecifications.createdOrUpdatedBy(request.getAccountId()));
        }

        if (request.getIsActive() != null) {
            specs.add(TvProgramSpecifications.isActive(BooleanConverter.getActiveBoolean(request.getIsActive())));
        }

        if (request.getIsPublish() != null) {
            specs.add(TvProgramSpecifications.isPublish(BooleanConverter.getActiveBoolean(request.getIsPublish())));
        }

        if (request.getIsKids() != null) {
            specs.add(TvProgramSpecifications.isKids(BooleanConverter.getActiveBoolean(request.getIsKids())));
        }

        if (request.getIsSport() != null) {
            specs.add(TvProgramSpecifications.isSports(BooleanConverter.getActiveBoolean(request.getIsSport())));
        }

        if (request.getHasRight() != null) {
            specs.add(TvProgramSpecifications.hasRight(BooleanConverter.getActiveBoolean(request.getHasRight())));
        }

        Query query = TvProgramQueryBuilder.build(specs);

        if (request.getName() != null && !request.getName().isEmpty()) {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                    .matching(request.getName());
            query.addCriteria(textCriteria);
        }

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        long total = mongoTemplate.count(query, TvProgram.class);
        query.with(pageable);

        List<TvProgram> tvProgramList = mongoTemplate.find(query, TvProgram.class);

        return new PageImpl<>(
                tvProgramAdminMapper.toFilterResponse(tvProgramList),
                pageable,
                total
        );
    }

    public TvProgram publishTvProgram(String tvProgramId) {
        TvProgram tvProgram = findTvProgramEntityById(tvProgramId);
        tvProgram.setIsPublish(true);
        tvProgramOpenSearchService.updateTvProgramSearchIndex(tvProgram);
        return tvProgramRepository.save(tvProgram);
    }

    public void addTvProgramToSection(String tvProgramId, Long sectionId, String isTop) throws IOException {
        TvProgram tvProgram = findTvProgramEntityById(tvProgramId);
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null));

        tvProgramOpenSearchService.addTvProgramToSection(
                tvProgram,
                section,
                TvProgramIndex.TV_PROGRAM_SECTION.getIndexName(),
                isTop
        );
    }

    public void removeTvProgramFromSection(Long sectionId, String tvProgramId) throws IOException {
        tvProgramOpenSearchService.removeTvProgramFromSection(
                sectionId,
                tvProgramId,
                TvProgramIndex.TV_PROGRAM_SECTION.getIndexName()
        );
    }

    public List<TvProgramSectionAdminResponse> getTvProgramSection(
            Long sectionId,
            String isTop,
            int page,
            int size
    ) throws IOException {
      return tvProgramOpenSearchService.getTvProgramSection(
                sectionId,
                TvProgramIndex.TV_PROGRAM_SECTION.getIndexName(),
                isTop,
                page,
                size
        );
    }

    public void updateTvProgramIsTop(Long sectionId, String tvProgramId, String isTop) throws IOException {
        tvProgramOpenSearchService.updateTvProgramIsTop(
                sectionId,
                tvProgramId,
                TvProgramIndex.TV_PROGRAM_SECTION.getIndexName(),
                isTop
        );
    }


}

