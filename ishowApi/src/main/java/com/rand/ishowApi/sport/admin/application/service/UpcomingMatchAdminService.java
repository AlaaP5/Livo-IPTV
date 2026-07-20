package com.rand.ishowApi.sport.admin.application.service;

import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelRepository;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.ChannelMeta;
import com.rand.ishowApi.metadata.ChampionMeta;
import com.rand.ishowApi.metadata.TeamMeta;
import com.rand.ishowApi.metadata.mapper.MetaHelper;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.sport.admin.api.request.UpcomingMatchAdminFilterRequest;
import com.rand.ishowApi.sport.admin.api.request.UpcomingMatchAdminRequest;
import com.rand.ishowApi.sport.admin.api.response.UpcomingMatchAdminResponse;
import com.rand.ishowApi.sport.admin.application.manager.UpcomingMatchDomainManager;
import com.rand.ishowApi.sport.admin.domain.entity.Champion;
import com.rand.ishowApi.sport.admin.domain.entity.Team;
import com.rand.ishowApi.sport.admin.domain.repository.ChampionRepository;
import com.rand.ishowApi.sport.admin.domain.repository.TeamRepository;
import com.rand.ishowApi.sport.admin.domain.specification.UpcomingMatchSpecification;
import com.rand.ishowApi.sport.admin.domain.specification.UpcomingMatchSpecifications;
import com.rand.ishowApi.sport.admin.mapper.UpcomingMatchAdminMapper;
import com.rand.ishowApi.sport.admin.domain.entity.UpcomingMatch;
import com.rand.ishowApi.sport.admin.domain.repository.UpcomingMatchRepository;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import com.rand.ishowApi.sport.admin.domain.specification.UpcomingMatchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpcomingMatchAdminService {

    private final UpcomingMatchDomainManager manager;
    private final UpcomingMatchRepository repository;
    private final UpcomingMatchAdminMapper mapper;
    private final MongoTemplate mongoTemplate;
    private final TeamRepository teamRepository;
    private final ChampionRepository championRepository;
    private final ChannelRepository channelRepository;
    private final MetaHelper metaHelper;

    public UpcomingMatchAdminResponse create(UpcomingMatchAdminRequest request){

        Team away = teamRepository.findById(request.awayTeamId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.TEAM_NOT_EXISTS, null));

        Team home = teamRepository.findById(request.awayTeamId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.TEAM_NOT_EXISTS, null));

        Champion champion = championRepository.findById(request.championId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHAMPION_NOT_EXISTS, null));

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null));

        TeamMeta homeTeam = metaHelper.createTeamMeta(home) ;
        TeamMeta awayTeam =  metaHelper.createTeamMeta(away) ;
        ChampionMeta championMeta = metaHelper.createChampionMeta(champion);
        ChannelMeta channelMeta = metaHelper.createChannelMeta(channel);

        UpcomingMatch match = manager.create(
            request,
            homeTeam,
            awayTeam,
            championMeta,
            channelMeta
        );
        match = repository.save(match);
        return mapper.toResponse(match);
    }

    public UpcomingMatchAdminResponse update(UpcomingMatchAdminRequest request){
        UpcomingMatch match = repository.findById(request.id())
                .orElseThrow(() -> new CustomException(ApiResponseCode.ERROR_404, null));

        Team away = teamRepository.findById(request.awayTeamId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.TEAM_NOT_EXISTS, null));

        Team home = teamRepository.findById(request.awayTeamId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.TEAM_NOT_EXISTS, null));

        Champion champion = championRepository.findById(request.championId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHAMPION_NOT_EXISTS, null));

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null));

        TeamMeta homeTeam = metaHelper.createTeamMeta(home) ;
        TeamMeta awayTeam =  metaHelper.createTeamMeta(away) ;
        ChampionMeta championMeta = metaHelper.createChampionMeta(champion);
        ChannelMeta channelMeta = metaHelper.createChannelMeta(channel);

        manager.update(
           request,
            homeTeam,
            awayTeam,
            championMeta,
            channelMeta,
            match
        );
        repository.save(match);
        return mapper.toResponse(match);
    }

    public UpcomingMatchAdminResponse changeStatus(String id, String active){
        UpcomingMatch match = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.ERROR_404, null));

        manager.activate(match, active);
        repository.save(match);
        return mapper.toResponse(match);
    }

    public Page<UpcomingMatchAdminResponse> filter(UpcomingMatchAdminFilterRequest request){

        // Build specifications list
        java.util.List<UpcomingMatchSpecification> specs = new java.util.ArrayList<>();

        if (request.getActive() != null) {
            specs.add(UpcomingMatchSpecifications.isActive(BooleanConverter.getActiveBoolean(request.getActive())));
        }

        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            specs.add(UpcomingMatchSpecifications.nameLike(request.getSearch()));
        }

        Query query = UpcomingMatchQueryBuilder.build(specs);

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        long total = mongoTemplate.count(query, UpcomingMatch.class);

        query.with(pageable);

        List<UpcomingMatch> list = mongoTemplate.find(query, UpcomingMatch.class);

        return new PageImpl<>(mapper.toResponseList(list), pageable, total);
    }


}

