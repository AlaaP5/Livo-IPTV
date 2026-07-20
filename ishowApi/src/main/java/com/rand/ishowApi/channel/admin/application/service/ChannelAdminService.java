package com.rand.ishowApi.channel.admin.application.service;


import com.rand.ishowApi.channel.admin.api.request.ChannelAdminRequest;
import com.rand.ishowApi.channel.admin.api.request.FilterChannelAdminRequest;
import com.rand.ishowApi.channel.admin.api.response.ChannelAdminFilterResponse;
import com.rand.ishowApi.channel.admin.api.response.ChannelAdminResponse;
import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.channel.admin.application.manager.ChannelDomainManager;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.openSearch.index.ChannelIndex;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelRepository;
import com.rand.ishowApi.channel.admin.domain.specification.ChannelQueryBuilder;
import com.rand.ishowApi.channel.admin.domain.specification.ChannelSpecification;
import com.rand.ishowApi.channel.admin.domain.specification.ChannelSpecifications;
import com.rand.ishowApi.channel.admin.mapper.ChannelAdminMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.admin.domain.repository.TagRepository;
import com.rand.ishowApi.metadata.mapper.MetaHelper;
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
public class ChannelAdminService {

    private final ChannelRepository channelRepository;
    private final TagRepository tagRepository;
    private final ChannelDomainManager channelDomainManager;
    private final ChannelAdminMapper channelMapper;
    private final UploadServiceAsync uploadServiceAsync;
    private final MongoTemplate mongoTemplate;
    private final MetaHelper metaHelper;
    private final SectionRepository sectionRepository;
    private final ChannelOpenSearchService channelOpenSearchService;


    // ==================== Manage Channel CRUD ====================

    public ChannelAdminResponse addChannel(ChannelAdminRequest request) {

        List<Tag> tagList = tagRepository.findAllById(request.getTags());
        List<TagMeta> tagListWithMeta = metaHelper.mapMetaTag(tagList);

        Channel channel = channelDomainManager.create(request, tagListWithMeta);

        channelRepository.save(channel);

        // async call for logo upload
        CompletableFuture<OriginalUploadMetadata> logoFuture =
                uploadServiceAsync.uploadTvChannelLogoAsync(request.getLogo(), channel.getId());

        // wait for upload
        OriginalUploadMetadata logoMetadata = logoFuture.join();

        //  set logo in entity via manager
        if (logoMetadata != null) {
            channelDomainManager.addChannelLogo(channel, logoMetadata);
        }

        Channel savedChannel = channelRepository.save(channel);

        // If channel is published, index it
        try {
            if (savedChannel.getIsPublish() != null && savedChannel.getIsPublish()) {
                channelOpenSearchService.updateSearchIndex(savedChannel);
            }
        } catch (Exception ignored) {
            // ignore indexing errors for now
        }

        return channelMapper.toResponse(savedChannel);
    }

    public ChannelAdminResponse updateChannel(ChannelAdminRequest request) throws IOException {

        // 1. Find existing channel
        Channel channel = channelRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null));

        // 2. Prepare tags if provided
        List<Tag> tags = null;
        if (request.getTags() != null) {
            tags = tagRepository.findAllById(request.getTags());
        }

        // 3. Prepare file upload future
        CompletableFuture<OriginalUploadMetadata> logoFuture = null;

        if (request.getLogo() != null) {
            logoFuture = uploadServiceAsync.uploadTvChannelLogoAsync(request.getLogo(), channel.getId());
        }

        // Wait for uploads to complete
        OriginalUploadMetadata logoMetadata = logoFuture != null ? logoFuture.join() : null;

        // Remove old file if new metadata is present (or null meaning remove)
        if (logoMetadata != null || (request.getLogo() != null)) {
            if (channel.getLogo() != null) {
                uploadServiceAsync.removeOldFileAsync(channel.getLogo());
            }
        }

        List<TagMeta> tagMetaList = metaHelper.mapMetaTag(tags);

        // Update channel with new metadata (null means keep existing, but we already removed old files)
        channelDomainManager.update(channel, request, tagMetaList, logoMetadata);

        Channel savedChannel = channelRepository.save(channel);

        // Update OpenSearch indexes if channel is published
        if (savedChannel.getIsPublish() != null && savedChannel.getIsPublish()) {
            try {
                channelOpenSearchService.updateChannelIndexes(savedChannel);
            } catch (Exception ignored) {
                // ignore indexing errors
            }
        }

        return channelMapper.toResponse(savedChannel);
    }

    public Channel findChannelById(String id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null));
    }

    public ChannelAdminResponse activateChannel(String id, String active) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null));

        channelDomainManager.setActive(channel, active);
        channelRepository.save(channel);
        return channelMapper.toResponse(channel);
    }

    public Page<ChannelAdminFilterResponse> filterChannels(FilterChannelAdminRequest request) {

        List<ChannelSpecification> specs = new ArrayList<>();

        if (request.getAccountId() != null) {
            specs.add(ChannelSpecifications.createdOrUpdatedBy(request.getAccountId()));
        }

        if (request.getIsActive() != null) {
            specs.add(ChannelSpecifications.isActive(BooleanConverter.getActiveBoolean(request.getIsActive())));
        }

        if (request.getIsPublish() != null) {
            specs.add(ChannelSpecifications.isPublish(BooleanConverter.getActiveBoolean(request.getIsPublish())));
        }

        if (request.getIsKids() != null) {
            specs.add(ChannelSpecifications.isKids(BooleanConverter.getActiveBoolean(request.getIsKids())));
        }

        if (request.getIsSport() != null) {
            specs.add(ChannelSpecifications.isSports(BooleanConverter.getActiveBoolean(request.getIsSport())));
        }

        if (request.getBadge() != null) {
            specs.add(ChannelSpecifications.hasBadge(request.getBadge()));
        }

        if (request.getTagId() != null) {
            specs.add(ChannelSpecifications.hasTag(request.getTagId()));
        }

        Query query = ChannelQueryBuilder.build(specs);

        // TEXT SEARCH ONLY (no regex)
        if (request.getName() != null && !request.getName().isEmpty()) {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                    .matching(request.getName());

            query.addCriteria(textCriteria);
        }

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        long total = mongoTemplate.count(query, Channel.class);

        query.with(pageable);

        List<Channel> channels = mongoTemplate.find(query, Channel.class);

        return new PageImpl<>(
                channelMapper.toFilterResponse(channels),
                pageable,
                total
        );
    }

    // ==================== Manage Channel Publishing ====================

    public Channel publishChannel(String channelId) throws IOException {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null));

        channelDomainManager.publishChannel(channel);

        channelOpenSearchService.updateSearchIndex(channel);

        return channelRepository.save(channel);
    }

    // ==================== Manage Channel Sections ====================

    public void addChannelToSection(String channelId, Long sectionId, String isTop) throws IOException {
        Channel channel = findChannelById(channelId);
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null));

        channelOpenSearchService.addChannelToSection(channel, section, ChannelIndex.CHANNEL_SECTION.getIndexName(), isTop);
    }

    public void removeChannelFromSection(Long sectionId, String channelId) {
        channelOpenSearchService.removeChannelFromSection(sectionId, channelId, ChannelIndex.CHANNEL_SECTION.getIndexName());
    }

    public List<ChannelAdminSectionResponse> getChannelsSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return channelOpenSearchService.getChannelsSection(sectionId, ChannelIndex.CHANNEL_SECTION.getIndexName(), isTop, page, size);
    }

    public void updateChannelIsTop(Long sectionId, String channelId, String isTop) throws IOException {
        channelOpenSearchService.updateChannelIsTop(sectionId, channelId, ChannelIndex.CHANNEL_SECTION.getIndexName(), isTop);
    }
}