package com.rand.ishowApi.clip.admin.application.service;
import com.rand.ishowApi.clip.admin.api.request.ClipAdminRequest;
import com.rand.ishowApi.clip.admin.api.request.FilterClipsAdminRequest;
import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.clip.admin.api.response.ClipsAdminResponse;
import com.rand.ishowApi.clip.admin.application.manager.ClipDomainManager;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.admin.domain.openSearch.index.ClipsIndex;
import com.rand.ishowApi.clip.admin.domain.repository.ClipRepository;
import com.rand.ishowApi.clip.admin.domain.specification.ClipQueryBuilder;
import com.rand.ishowApi.clip.admin.domain.specification.ClipSpecification;
import com.rand.ishowApi.clip.admin.domain.specification.ClipSpecifications;
import com.rand.ishowApi.clip.admin.mapper.ClipAdminMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.mapper.MetaHelper;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
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
public class ClipAdminService {
    private final ClipRepository clipRepository;
    private final TagRepository tagRepository;
    private final ClipDomainManager clipDomainManager;
    private final ClipAdminMapper clipMapper;
    private final UploadServiceAsync uploadServiceAsync;
    private final MongoTemplate mongoTemplate;
    private final MetaHelper metaHelper;
    private final SectionRepository sectionRepository;
    private final ClipOpenSearchService clipOpenSearchService;

    public Clip createClip(ClipAdminRequest request) {
        List<Tag> tagList = request.getTags() != null ? tagRepository.findAllById(request.getTags()) : null;
        List<TagMeta> tagMetaList = metaHelper.mapMetaTag(tagList);

        Clip clip = clipDomainManager.create(request, tagMetaList);
        clipRepository.save(clip);

        CompletableFuture<OriginalUploadMetadata> originalFuture =
                uploadServiceAsync.uploadClipVideoAsync(request.getFile(), clip.getId());

        CompletableFuture<OriginalUploadMetadata> posterFuture =
                uploadServiceAsync.uploadClipPosterAsync(request.getPoster(), clip.getId());

        CompletableFuture.allOf(originalFuture, posterFuture).join();

        clipDomainManager.addClipFiles(clip, originalFuture.join(), posterFuture.join());

        return clipRepository.save(clip);
    }


    public Clip findClipById(String id) {
        return clipRepository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.CLIP_NOT_FOUND, null));
    }

    public Clip updateClip(ClipAdminRequest request) throws IOException {
        Clip clip = findClipById(request.getClipId());

        List<Tag> tagList = request.getTags() != null ? tagRepository.findAllById(request.getTags()) : null;
        List<TagMeta> tags = metaHelper.mapMetaTag(tagList);

        List<CompletableFuture<?>> futures = new ArrayList<>();
        CompletableFuture<OriginalUploadMetadata> originalFuture = null;
        CompletableFuture<OriginalUploadMetadata> posterFuture = null;

        if (request.getFile() != null) {
            originalFuture = uploadServiceAsync.uploadClipVideoAsync(request.getFile(), clip.getId());
            futures.add(originalFuture);
        }
        if (request.getPoster() != null) {
            posterFuture = uploadServiceAsync.uploadClipPosterAsync(request.getPoster(), clip.getId());
            futures.add(posterFuture);
        }
        if (!futures.isEmpty()) {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
        OriginalUploadMetadata originalMetadata = originalFuture != null ? originalFuture.join() : null;
        OriginalUploadMetadata posterMetadata = posterFuture != null ? posterFuture.join() : null;
        if (originalMetadata != null || request.getFile() != null) {
            if (clip.getOriginalUploadMetadata() != null) {
                uploadServiceAsync.removeOldFileAsync(clip.getOriginalUploadMetadata());
            }
        }
        if (posterMetadata != null || request.getPoster() != null) {
            if (clip.getPoster() != null) {
                uploadServiceAsync.removeOldFileAsync(clip.getPoster());
            }
        }
        clipDomainManager.update(clip, request, tags, originalMetadata, posterMetadata);
        clip = clipRepository.save(clip);

        // Update indexes if clip is published
        if (clip.getIsPublish()) {
            clipOpenSearchService.updateClipIndexes(clip);
        }

        return clip;
    }

    public Page<ClipsAdminResponse> filterClips(FilterClipsAdminRequest request) {
        List<ClipSpecification> specs = new ArrayList<>();
        if (request.getStatus() != null) {
            specs.add(ClipSpecifications.hasTranscodeStatus(request.getStatus()));
        }
        if (request.getAccessType() != null) {
            specs.add(ClipSpecifications.hasAccessType(request.getAccessType()));
        }
        if (request.getBadge() != null) {
            specs.add(ClipSpecifications.hasBadge(request.getBadge()));
        }
        if (request.getAccountId() != null) {
            specs.add(ClipSpecifications.createdOrUpdatedBy(request.getAccountId()));
        }
        if (request.getIsActive() != null) {
            specs.add(ClipSpecifications.isActive(BooleanConverter.getActiveBoolean(request.getIsActive())));
        }
        if (request.getIsPublish() != null) {
            specs.add(ClipSpecifications.isPublish(BooleanConverter.getActiveBoolean(request.getIsPublish())));
        }
        if (request.getIsKids() != null) {
            specs.add(ClipSpecifications.isKids(BooleanConverter.getActiveBoolean(request.getIsKids())));
        }
        if (request.getIsSport() != null) {
            specs.add(ClipSpecifications.isSports(BooleanConverter.getActiveBoolean(request.getIsSport())));
        }
        if (request.getHasRight() != null) {
            specs.add(ClipSpecifications.hasRight(BooleanConverter.getActiveBoolean(request.getHasRight())));
        }
        Query query = ClipQueryBuilder.build(specs);
        if (request.getName() != null && !request.getName().isBlank()) {
            query.addCriteria(TextCriteria.forDefaultLanguage().matching(request.getName()));
        }

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);
        long total = mongoTemplate.count(query, Clip.class);
        query.with(pageable);

        List<Clip> clips = mongoTemplate.find(query, Clip.class);

        return new PageImpl<>(clipMapper.toFilterResponse(clips), pageable, total);
    }


    public Clip publishClip(String clipId) throws IOException {
        Clip clip = clipRepository.findById(clipId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.CLIP_NOT_FOUND, null));
        if (clip.getTranscodeStatus() != TranscodeStatus.COMPLETED) {
            throw new CustomException(ApiResponseCode.CLIP_TRANSCODE_NOT_COMPLETE, null);
        }
        clipDomainManager.publishClip(clip);


        try {
            clipOpenSearchService.updateSearchIndex(clip);
        } catch (Exception ignored) {
            throw new CustomException(ApiResponseCode.CLIP_NOT_PUBLISH, null);
        }
        Clip savedClip = clipRepository.save(clip);
        return savedClip;
    }

    public void addClipToSection(String clipId, Long sectionId, String isTop) throws IOException {
        Clip clip = findClipById(clipId);
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null));

        clipOpenSearchService.addClipToSection(clip, section, ClipsIndex.CLIPS_SECTION.getIndexName(), isTop);
    }

    public void removeClipFromSection(Long sectionId, String clipId) {
        clipOpenSearchService.removeClipFromSection(sectionId, clipId, ClipsIndex.CLIPS_SECTION.getIndexName());
    }

    public List<ClipSectionAdminResponse> getClipsSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return clipOpenSearchService.getClipsSection(sectionId, ClipsIndex.CLIPS_SECTION.getIndexName(), isTop, page, size);
    }

    public void updateClipIsTop(Long sectionId, String clipId, String isTop) throws IOException {
        clipOpenSearchService.updateClipIsTop(sectionId, clipId, ClipsIndex.CLIPS_SECTION.getIndexName(), isTop);
    }

    public void updateClipTranscodeResult(String clipId, TranscodeMetaData metaData) throws IOException {
        Clip clip = findClipById(clipId);
        clipDomainManager.addClipTranscodeFile(clip, metaData);
        clip = clipRepository.save(clip);

        if (clip.getIsPublish()) {
            clipOpenSearchService.updateClipIndexes(clip);
        }
    }

}
