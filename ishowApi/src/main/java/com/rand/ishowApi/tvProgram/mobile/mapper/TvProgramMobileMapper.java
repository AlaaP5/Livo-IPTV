package com.rand.ishowApi.tvProgram.mobile.mapper;


import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramEpisode;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import com.rand.ishowApi.tvProgram.mobile.api.response.TVProgramSeasonListResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramDetailsResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramEpisodeDetailsResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSeasonDetailsResponse;
import com.rand.ishowApi.utils.DurationConverter;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class TvProgramMobileMapper {

    public TvProgramDetailsResponse toTvProgramDetailsResponse(TvProgram tvProgram,
                                                               List<TvProgramSeason> seasonList,
                                                               List<TvProgram> relatedTvPrograms,
                                                               boolean isLke,
                                                               boolean isWatchList) {

        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        TvProgramDetailsResponse response = new TvProgramDetailsResponse();

        // Set basic tvProgram fields
        response.setTvProgramId(tvProgram.getId());
        response.setTitle(LocalizedText.getName(tvProgram.getTitleEn(), tvProgram.getTitleAr(), lang));
        response.setDescription(LocalizedText.getName(tvProgram.getDescriptionEn(), tvProgram.getDescriptionAr(), lang));

        if (tvProgram.getPoster() != null) {
            response.setPoster(tvProgram.getPoster().getGeneratedPath());
        }

        response.setBadge(tvProgram.getBadge());
        response.setReleaseYear(tvProgram.getReleaseYear());
        response.setSeasonCount(tvProgram.getSeasonCount());
        response.setSubtitleLanguages(tvProgram.getSubtitleLanguages());
        response.setAudioLanguages(tvProgram.getAudioLanguages());

        // Trailer from the first season if available
        if (seasonList != null && !seasonList.isEmpty()) {
            seasonList.stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .ifPresent(firstSeason -> {
                        if (firstSeason.getTrailer() != null && firstSeason.getTrailer().getTranscodeMetaData() != null) {
                            //response.setTrailerUrl(firstSeason.getTrailer().getTranscodeMetaData().getMasterPlaylist());
                            response.setTrailerUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
                        }
                    });
        }

        // Tags
        if (tvProgram.getTags() != null) {
            response.setTags(
                tvProgram.getTags().stream().map(tag -> {
                    LookupResponse t = new LookupResponse();
                    t.setId(tag.getId());
                    t.setName(LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang));
                    return t;
                }).toList()
            );
        }

        // Language
        if (tvProgram.getLanguage() != null) {
            response.setLanguage(tvProgram.getLanguage().name());
        }

        response.setIsLike(isLke);
        response.setIsWatchList(isWatchList);

        // Seasons
        if (seasonList != null) {
            response.setSeasons(mapSeasonList(seasonList, lang));
        }

        // Related tvPrograms
        if (relatedTvPrograms != null) {
            response.setRelated(
                relatedTvPrograms.stream().map(related-> mapRelatedTvProgram(related,lang)).toList()
            );
        }

        return response;
    }

    public TvProgramSeasonDetailsResponse mapSeasonDetails(TvProgramSeason season, List<TvProgramEpisode> episodes) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();
        TvProgramSeasonDetailsResponse response = new TvProgramSeasonDetailsResponse();

        response.setSeasonId(season.getId());
        response.setTitle(LocalizedText.getName(season.getTitleEn(), season.getTitleAr(), lang));
        response.setDescription(LocalizedText.getName(season.getDescriptionEn(), season.getDescriptionAr(), lang));

        if (season.getPoster() != null) {
            response.setPoster(season.getPoster().getGeneratedPath());
        }

        response.setReleaseYear(season.getReleaseYear());
        response.setNumber(season.getSeasonNumber());
        response.setEpisodeCount(season.getEpisodeCount());

        if (season.getTrailer() != null && season.getTrailer().getTranscodeMetaData() != null) {
            //response.setTrailerUrl(season.getTrailer().getTranscodeMetaData().getMasterPlaylist());
            response.setTrailerUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
        }

        if (episodes != null) {
            List<TvProgramEpisodeDetailsResponse> episodeList = episodes.stream()
                    .map(this::mapEpisode)
                    .toList();
            response.setEpisodes(episodeList);
        }

        response.setIsWatchList(false); // TODO: from user service

        return response;
    }

    private TvProgramEpisodeDetailsResponse mapEpisode(TvProgramEpisode episode) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();
        TvProgramEpisodeDetailsResponse response = new TvProgramEpisodeDetailsResponse();

        response.setEpisodeId(episode.getId());
        response.setEpisodeNumber(episode.getEpisodeNumber());
        response.setTitle(LocalizedText.getName(episode.getTitleEn(), episode.getTitleAr(), lang));

        if (episode.getPoster() != null) {
            response.setPoster(episode.getPoster().getGeneratedPath());
        }

        response.setAccessType(episode.getAccessType());
        response.setBadge(episode.getBadge());
        response.setDuration(DurationConverter.toTimeFormat( episode.getDuration()));

        if (episode.getTranscodeMetaData() != null) {
            //response.setStreamUrl(episode.getTranscodeMetaData().getMasterPlaylist());
            response.setStreamUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
        }


        if (episode.getSubtitles() != null) {
            response.setSubtitles(
                episode.getSubtitles().stream()
                    .map(OriginalUploadMetadata::getGeneratedPath)
                    .toList()
            );
        }

        return response;
    }

    private List<TVProgramSeasonListResponse> mapSeasonList(List<TvProgramSeason> seasonList, String lang) {
        return seasonList.stream().map(season -> {
            TVProgramSeasonListResponse response = new TVProgramSeasonListResponse();
            response.setSeasonId(season.getId());
            response.setTitle(LocalizedText.getName(season.getTitleEn(), season.getTitleAr(), lang));
            response.setDescription(LocalizedText.getName(season.getDescriptionEn(), season.getDescriptionAr(), lang));

            if (season.getPoster() != null) {
                response.setPoster(season.getPoster().getGeneratedPath());
            }

            response.setReleaseYear(season.getReleaseYear());
            response.setNumber(season.getSeasonNumber());
            response.setEpisodeCount(season.getEpisodeCount());

            return response;
        }).toList();
    }

    private TvProgramDetailsResponse mapRelatedTvProgram(TvProgram tvProgram,String lang) {
        TvProgramDetailsResponse response = new TvProgramDetailsResponse();
        response.setTvProgramId(tvProgram.getId());
        response.setTitle(LocalizedText.getName(tvProgram.getTitleEn(), tvProgram.getTitleAr(), lang));
        if (tvProgram.getPoster() != null) {
            response.setPoster(tvProgram.getPoster().getGeneratedPath());
        }
        response.setBadge(tvProgram.getBadge());
        response.setReleaseYear(tvProgram.getReleaseYear());
        response.setSeasonCount(tvProgram.getSeasonCount());
        response.setSubtitleLanguages(tvProgram.getSubtitleLanguages());
        response.setAudioLanguages(tvProgram.getAudioLanguages());
        // Do not set seasons or related to avoid recursion and heavy payload
        return response;
    }

}
