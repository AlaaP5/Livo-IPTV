package com.rand.ishowApi.sport.mobile.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.channel.mobile.mapper.ChannelDocMapper;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import com.rand.ishowApi.clip.mobile.mapper.ClipDocMapper;
import com.rand.ishowApi.metadata.ChampionMeta;
import com.rand.ishowApi.metadata.ChannelMeta;
import com.rand.ishowApi.metadata.TeamMeta;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import com.rand.ishowApi.movie.mobile.mapper.MovieDocMapper;
import com.rand.ishowApi.openSearch.mapper.SectionAggregationMapper;
import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import com.rand.ishowApi.series.mobile.mapper.SeriesDocMapper;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.sport.admin.domain.entity.UpcomingMatch;
import com.rand.ishowApi.sport.mobile.api.response.*;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import com.rand.ishowApi.tvProgram.mobile.mapper.TvProgramDocMapper;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.core.MsearchResponse;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SportMobileMapper {

    private final SectionAggregationMapper sectionAggregationMapper;
    private final MovieDocMapper movieDocMapper;
    private final SeriesDocMapper seriesDocMapper;
    private final TvProgramDocMapper tvProgramDocMapper;
    private final ClipDocMapper clipDocMapper;
    private final ChannelDocMapper channelDocMapper;

    public SportMobileResponse mapToSportMobileResponse(MsearchResponse<JsonNode> result, List<UpcomingMatch> upcomingMatches, String lang) {
        SportMobileResponse sportMobileResponse = new SportMobileResponse();

        sportMobileResponse.setUpcomingMatches(mapSportUpcomingMatch(
                upcomingMatches,
                lang
        ));

        List<SportSectionResponse> sections = new ArrayList<>();
        sections.addAll(mapSportSectionsByContentType(result, 0, MovieDocument.class, movieDocMapper, ContentType.MOVIES, lang));
        sections.addAll(mapSportSectionsByContentType(result, 1, SeriesDocument.class, seriesDocMapper, ContentType.SERIES, lang));
        sections.addAll(mapSportSectionsByContentType(result, 2, TvProgramDocument.class, tvProgramDocMapper, ContentType.TV_PROGRAMS, lang));
        sections.addAll(mapSportSectionsByContentType(result, 3, ClipDocument.class, clipDocMapper, ContentType.CLIPS, lang));
        sections.addAll(mapSportSectionsByContentType(result, 4, ChannelDocument.class, channelDocMapper, ContentType.CHANNELS, lang));

        sections.sort(Comparator.comparing(
                SportSectionResponse::getSectionOrder,
                Comparator.nullsLast(Integer::compareTo)
        ));

        sportMobileResponse.setSections(sections);
        return sportMobileResponse;
    }

    private List<UpcomingMatchResponse> mapSportUpcomingMatch(List<UpcomingMatch> upcomingMatches, String lang) {
        if (upcomingMatches == null) {
            return List.of();
        }

        return upcomingMatches.stream()
                .map(upcomingMatch -> mapUpcomingMatch(upcomingMatch, lang))
                .toList();
    }

    private SportTeamResponse mapTeam(TeamMeta team, String lang) {
        if (team == null) {
            return null;
        }

        SportTeamResponse response = new SportTeamResponse();
        response.setId(team.getId());
        response.setName(LocalizedText.getName(
                team.getNameEn(),
                team.getNameAr(),
                lang
        ));
        response.setPoster(team.getGeneratedImagePath());

        return response;
    }

    private SportChampionResponse mapChampion(ChampionMeta champion, String lang) {
        if (champion == null) {
            return null;
        }

        SportChampionResponse response = new SportChampionResponse();
        response.setId(champion.getId());
        response.setName(LocalizedText.getName(
                champion.getNameEn(),
                champion.getNameAr(),
                lang
        ));
        response.setPoster(champion.getGeneratedImagePath());

        return response;
    }

    private SportChannelResponse mapChannel(ChannelMeta channel, String lang) {
        if (channel == null) {
            return null;
        }

        SportChannelResponse response = new SportChannelResponse();
        response.setId(channel.getId());
        response.setTitle(LocalizedText.getName(
                channel.getTitleEn(),
                channel.getTitleAr(),
                lang
        ));
        response.setPoster(channel.getLogo().getGeneratedPath());

        return response;
    }


    private UpcomingMatchResponse mapUpcomingMatch(UpcomingMatch upcomingMatch, String lang) {
        UpcomingMatchResponse response = new UpcomingMatchResponse();

        response.setId(upcomingMatch.getId());
        response.setMatchDate(upcomingMatch.getMatchDate());
        response.setHomeTeam(mapTeam(upcomingMatch.getHomeTeam(), lang));
        response.setAwayTeam(mapTeam(upcomingMatch.getAwayTeam(), lang));
        response.setTournament(mapChampion(upcomingMatch.getChampion(), lang));
        response.setChannel(mapChannel(upcomingMatch.getChannel(), lang));

        return response;
    }

    private <D extends SectionDocument, R> List<SportSectionResponse> mapSportSectionsByContentType(
            MsearchResponse<JsonNode> result,
            int responseIndex,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            ContentType type,
            String lang
    ) {
        List<D> documents = sectionAggregationMapper.mapSources(result, responseIndex, documentClass);
        Map<Long, List<D>> bySectionId = new LinkedHashMap<>();

        for (D document : documents) {
            if (document.getSectionId() == null) {
                continue;
            }
            bySectionId.computeIfAbsent(document.getSectionId(), ignored -> new ArrayList<>()).add(document);
        }

        return bySectionId.values().stream()
                .map(sectionDocuments -> mapSportSectionGroup(sectionDocuments, mapper, type, lang))
                .sorted(Comparator.comparing(
                        SportSectionResponse::getSectionOrder,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .toList();
    }

    private <D extends SectionDocument, R> SportSectionResponse mapSportSectionGroup(
            List<D> sectionDocuments,
            SectionContentMapper<D, R> mapper,
            ContentType type,
            String lang
    ) {
        D first = sectionDocuments.getFirst();

        SportSectionResponse response = new SportSectionResponse();
        response.setSectionId(first.getSectionId());
        response.setSectionTitle(LocalizedText.getName(first.getSectionTitleEn(), first.getSectionTitleAr(), lang));
        response.setSectionType(type);
        response.setSectionOrder(first.getSectionOrder());
        response.setContents(sectionDocuments.stream()
                .map(document -> mapper.map(document, lang))
                .toList());

        return response;
    }
}
