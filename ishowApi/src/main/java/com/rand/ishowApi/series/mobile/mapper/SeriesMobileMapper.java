package com.rand.ishowApi.series.mobile.mapper;

import com.rand.ishowApi.lookup.api.response.LookupPosterResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.entity.SeriesEpisode;
import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.series.mobile.api.response.EpisodeDetailsResponse;
import com.rand.ishowApi.series.mobile.api.response.SeasonListResponse;
import com.rand.ishowApi.series.mobile.api.response.SeriesDetailsResponse;
import com.rand.ishowApi.series.mobile.api.response.SeasonDetailsResponse;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.utils.DurationConverter;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeriesMobileMapper {

    public SeriesDetailsResponse toSeriesDetailsResponse(Series series, List<SeriesSeason> seasonList,
                                                         List<Series> relatedSeries,
                                                         boolean isLke,
                                                         boolean isWatchList) {

        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        SeriesDetailsResponse response = new SeriesDetailsResponse();

        // Set basic series fields
        response.setSeriesId(series.getId());
        response.setTitle(LocalizedText.getName(series.getTitleEn(), series.getTitleAr(), lang));
        response.setDescription(LocalizedText.getName(series.getDescriptionEn(), series.getDescriptionAr(), lang));

        if (series.getPoster() != null) {
            response.setPoster(series.getPoster().getGeneratedPath());
        }

        // Trailer from the first season if available
        if (seasonList != null && !seasonList.isEmpty()) {
            SeriesSeason firstSeason = seasonList.stream().findFirst().orElse(null);
            if (firstSeason.getTrailer() != null && firstSeason.getTrailer().getTranscodeMetaData() != null) {
                //response.setTrailerUrl(firstSeason.getTrailer().getTranscodeMetaData().getMasterPlaylist());
                response.setTrailerUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
            }
        }

        response.setBadge(series.getBadge());
        response.setReleaseYear(series.getReleaseYear());
        response.setSubtitleLanguages(series.getSubtitleLanguages());
        response.setAudioLanguages(series.getAudioLanguages());
        response.setRating(series.getRating() != null ? series.getRating() : 0.0);

        // Tags
        if (series.getTags() != null) {
            response.setTags(
                series.getTags().stream().map(tag -> {
                    LookupResponse t = new LookupResponse();
                    t.setId(tag.getId());
                    t.setName(LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang));
                    return t;
                }).toList()
            );
        }

        // Language
        if (series.getLanguage() != null) {
            response.setLanguage(series.getLanguage().name());
        }

        // Actors
        if (series.getActors() != null) {
            response.setActors(
                series.getActors().stream().map(actor -> {
                    LookupPosterResponse a = new LookupPosterResponse();
                    a.setId(actor.getId());
                    a.setName(LocalizedText.getName(actor.getNameEn(), actor.getNameAr(), lang));
                    a.setPoster(actor.getGeneratedImagePath());
                    return a;
                }).toList()
            );
        }

        response.setIsLike(isLke);
        response.setIsWatchList(isWatchList);

        // Seasons
        if (seasonList != null) {
                response.setSeasons(mapSeasonList(seasonList,lang));
        }

        // Related series
        if (relatedSeries != null) {
            response.setRelated(
                relatedSeries.stream().map(this::mapRelatedSeries).toList()
            );
        }

        return response;
    }

    public SeasonDetailsResponse mapSeasonDetails(SeriesSeason season, List<SeriesEpisode> episodes) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();
        SeasonDetailsResponse response = new SeasonDetailsResponse();

        response.setSeasonId(season.getId());
        response.setTitle(LocalizedText.getName(season.getTitleEn(), season.getTitleAr(), lang));
        response.setDescription(LocalizedText.getName(season.getDescriptionEn(), season.getDescriptionAr(), lang));

        if (season.getPoster() != null) {
            response.setPoster(season.getPoster().getGeneratedPath());
        }

        response.setReleaseYear(season.getReleaseYear());
        response.setRating(season.getRating() != null ? season.getRating(): 0.0);
        response.setNumber(season.getSeasonNumber());
        // Rating not available at season level
        response.setEpisodeCount(season.getEpisodeCount());

        if (season.getTrailer() != null && season.getTrailer().getTranscodeMetaData() != null) {
            //response.setTrailerUrl(season.getTrailer().getTranscodeMetaData().getMasterPlaylist());
            response.setTrailerUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
        }

        if (season.getActors() != null) {
            response.setActors(
                season.getActors().stream().map(actor -> {
                    LookupPosterResponse a = new LookupPosterResponse();
                    a.setId(actor.getId());
                    a.setName(LocalizedText.getName(actor.getNameEn(), actor.getNameAr(), lang));
                    a.setPoster(actor.getGeneratedImagePath());
                    return a;
                }).toList()
            );
        }
        List<EpisodeDetailsResponse> episodeList = episodes.stream()
                .map(this::mapEpisode)
                .toList();

        response.setEpisodes(episodeList);
        response.setIsWatchList(false); // TODO

        return response;
    }
    private List<SeasonListResponse> mapSeasonList(List<SeriesSeason> seasonList,String lang) {
        return seasonList.stream().map(season -> {
            SeasonListResponse response = new SeasonListResponse();
            response.setSeasonId(season.getId());

            response.setTitle(LocalizedText.getName(season.getTitleEn(),season.getTitleAr(),lang));
            response.setDescription(LocalizedText.getName(season.getDescriptionEn(),season.getDescriptionAr(),lang));

            if (season.getPoster() != null) {
                response.setPoster(season.getPoster().getGeneratedPath()); // adjust if field name differs
            }

            response.setReleaseYear(season.getReleaseYear());
            response.setNumber(season.getSeasonNumber());
            response.setRating(season.getRating());
            response.setEpisodeCount(season.getEpisodeCount());

            return response;
        }).toList();
    }
    private SeriesDetailsResponse mapRelatedSeries(Series series) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();
        SeriesDetailsResponse response = new SeriesDetailsResponse();
        response.setSeriesId(series.getId());
        response.setTitle(LocalizedText.getName(series.getTitleEn(), series.getTitleAr(), lang));
        if (series.getPoster() != null) {
            response.setPoster(series.getPoster().getGeneratedPath());
        }
        response.setBadge(series.getBadge());
        response.setRating(series.getRating() != null ? series.getRating() : 0.0);
        response.setReleaseYear(series.getReleaseYear());
        // Do not set seasons or related to avoid recursion and heavy payload
        return response;
    }

    private EpisodeDetailsResponse mapEpisode(SeriesEpisode episode) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();
        EpisodeDetailsResponse response = new EpisodeDetailsResponse();

        response.setEpisodeId(episode.getId());
        response.setEpisodeNumber(episode.getEpisodeNumber());
        response.setTitle(LocalizedText.getName(episode.getTitleEn(), episode.getTitleAr(), lang));

        if (episode.getPoster() != null) {
            response.setPoster(episode.getPoster().getGeneratedPath());
        }

        response.setRating(episode.getRate() != null ? episode.getRate() : 0.0);
        response.setAccessType(episode.getAccessType());
        response.setBadge(episode.getBadge());
        response.setDuration(DurationConverter.toTimeFormat(episode.getDuration()));

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
}
