package com.rand.ishowApi.channel.admin.application.service;

import com.rand.ishowApi.channel.admin.api.request.ChannelEpgAdminRequest;
import com.rand.ishowApi.channel.admin.api.request.FilterChannelEpgAdminRequest;
import com.rand.ishowApi.channel.admin.api.response.ChannelEpgAdminResponse;
import com.rand.ishowApi.channel.admin.application.manager.ChannelEpgDomainManager;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.entity.ChannelEpg;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelEpgRepository;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelRepository;
import com.rand.ishowApi.channel.admin.domain.specification.ChannelEpgQueryBuilder;
import com.rand.ishowApi.channel.admin.domain.specification.ChannelEpgSpecification;
import com.rand.ishowApi.channel.admin.domain.specification.ChannelEpgSpecifications;
import com.rand.ishowApi.channel.admin.mapper.ChannelEpgAdminMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.upload.service.UploadService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelEpgAdminService {

    private final ChannelEpgRepository channelEpgRepository;
    private final ChannelEpgDomainManager channelEpgDomainManager;
    private final ChannelEpgAdminMapper mapper;
    private final MongoTemplate mongoTemplate;
    private final UploadService uploadService;
    private final ChannelRepository channelRepository;


    public ChannelEpgAdminResponse add(ChannelEpgAdminRequest req) {

        Channel channel = channelRepository.findById(req.getChannelId())
                .orElseThrow(() -> new CustomException("Channel not found", null));

        ChannelEpg epg = channelEpgDomainManager.create(req);
        channelEpgRepository.save(epg);
        return mapper.toResponse(epg, channel);
    }

    @Transactional
    public List<ChannelEpgAdminResponse> addAll(MultipartFile xmlFile, String channelId) throws IOException {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException("Channel not found", null));

        List<ChannelEpgAdminRequest> responses =
                uploadService.parseEpgXml(xmlFile, channelId);


        if (responses == null || responses.isEmpty()) {
            throw new CustomException(ApiResponseCode.ERROR_FILE_EMPTY, null);
        }

        List<ChannelEpg> existingEpgs = channelEpgRepository.findByChannelIdAndActiveTrueOrderByStartDateAsc(channelId);
        if (existingEpgs != null && !existingEpgs.isEmpty()) {
            existingEpgs.forEach(epg -> epg.setActive(false));
            channelEpgRepository.saveAll(existingEpgs);
        }

        List<ChannelEpg> epgs = responses.stream()
                .map(channelEpgDomainManager::createFromParsedXml)
                .toList();

        List<ChannelEpg> saved = channelEpgRepository.saveAll(epgs);
        return mapper.toFilterResponse(saved, channel);
    }

    public ChannelEpgAdminResponse update(ChannelEpgAdminRequest req) {

        Channel channel = channelRepository.findById(req.getChannelId())
                .orElseThrow(() -> new CustomException("Channel not found", null));

        ChannelEpg epg = channelEpgRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException("Channel EPG not found", null));

        channelEpgDomainManager.update(epg, req);

        ChannelEpg saved = channelEpgRepository.save(epg);

        return mapper.toResponse(saved, channel);
    }

    public ChannelEpgAdminResponse activate(String id, String active) {

        ChannelEpg epg = channelEpgRepository.findById(id)
                .orElseThrow(() -> new CustomException("Channel EPG not found", null));

        Channel channel = channelRepository.findById(epg.getChannelId())
                .orElseThrow(() -> new CustomException("Channel not found", null));

        channelEpgDomainManager.setActive(epg, active);
        channelEpgRepository.save(epg);
        return mapper.toResponse(epg, channel);
    }

    public Page<ChannelEpgAdminResponse> filter(FilterChannelEpgAdminRequest request) {

        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new CustomException("Channel not found", null));

        List<ChannelEpgSpecification> specs = new ArrayList<>();

        if (request.getChannelId() != null) {
            specs.add(ChannelEpgSpecifications.hasChannelId(request.getChannelId()));
        }

        if (request.getActive() != null) {
            specs.add(ChannelEpgSpecifications.isActive(BooleanConverter.getActiveBoolean(request.getActive())));
        }

        if (request.getStartDate() != null) {
            LocalDateTime start = LocalDateTime.parse(request.getStartDate());
            specs.add(ChannelEpgSpecifications.startDateGte(start));
        }

        if (request.getEndDate() != null) {
            LocalDateTime end = LocalDateTime.parse(request.getEndDate());
            specs.add(ChannelEpgSpecifications.endDateLte(end));
        }

        Query query = ChannelEpgQueryBuilder.build(specs);

        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(request.getTitle());
            query.addCriteria(textCriteria);
        }

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        long total = mongoTemplate.count(query, ChannelEpg.class);

        query.with(pageable);

        List<ChannelEpg> list = mongoTemplate.find(query, ChannelEpg.class);

        return new PageImpl<>(
                mapper.toFilterResponse(list, channel),
                pageable,
                total
        );
    }
}

