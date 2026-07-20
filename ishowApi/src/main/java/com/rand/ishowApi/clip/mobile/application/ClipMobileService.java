package com.rand.ishowApi.clip.mobile.application;


import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.admin.domain.openSearch.index.ClipsIndex;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import com.rand.ishowApi.clip.admin.domain.repository.ClipRepository;
import com.rand.ishowApi.clip.admin.domain.specification.ClipQueryBuilder;
import com.rand.ishowApi.clip.admin.domain.specification.ClipSpecification;
import com.rand.ishowApi.clip.admin.domain.specification.ClipSpecifications;
import com.rand.ishowApi.clip.mobile.api.response.ClipSectionResponse;
import com.rand.ishowApi.clip.mobile.api.response.ClipsDetailsResponse;
import com.rand.ishowApi.clip.mobile.mapper.ClipDocMapper;
import com.rand.ishowApi.clip.mobile.mapper.ClipMobileMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.favorite.application.service.FavoriteService;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.openSearch.service.GenericSectionService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.application.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClipMobileService {
    private final GenericSectionService sectionService;
    private final ClipDocMapper docMapper;
    private final ClipMobileMapper mapper;
    private final ClipRepository clipRepository;
    private final MongoTemplate mongoTemplate;
    private final WatchListService watchListService;
    private final FavoriteService favoriteService;


    public SectionBannerResponse<ClipSectionResponse> getClipsSections() throws IOException {

        return sectionService.getSections(
                ClipsIndex.CLIPS_SECTION.getIndexName(),
                ClipDocument.class,
                ContentType.CLIPS,
                docMapper,
                true
        );
    }

    public List<ClipSectionResponse> getClipsSectionById(Long sectionId) throws IOException {
        return sectionService.getSectionContent(
                ClipsIndex.CLIPS_SECTION.getIndexName(),
                ClipDocument.class,
                docMapper,
                sectionId,
                true
        );
    }

    public ClipsDetailsResponse getClipDetails(String clipId) {
        Clip clip = clipRepository.findById(clipId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.CLIP_NOT_FOUND, null));

        List<Clip> relatedClips = getRelatedClips(clip);

        boolean isLke = favoriteService.isInFavorite(clipId, ContentType.CLIPS);
        boolean isWatchList = watchListService.isInWatchList(clipId, ContentType.CLIPS);


        return mapper.toClipDetails(clip, relatedClips, isLke, isWatchList);
    }

    private List<Clip> getRelatedClips(Clip clip) {
        List<Long> tagIds= clip.getTags()
                .stream()
                .map(TagMeta::getId)
                .toList();



        List<ClipSpecification> specs = List.of(
                ClipSpecifications.isActive(true),
                ClipSpecifications.isPublish(true),
                ClipSpecifications.isKids(clip.getIsKids()),
                ClipSpecifications.isSports(clip.getIsSports()),
                ClipSpecifications.hasAnyTagIds(tagIds),
                ClipSpecifications.excludeClip(clip.getId())
        );
        Query query = ClipQueryBuilder.build(specs);
        query.limit(5);
        return mongoTemplate.find(query, Clip.class);


    }

}
