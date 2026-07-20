package com.rand.ishowApi.sport.admin.application.service;

import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.channel.admin.application.service.ChannelOpenSearchService;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.openSearch.index.ChannelIndex;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelRepository;
import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.clip.admin.application.service.ClipOpenSearchService;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.admin.domain.openSearch.index.ClipsIndex;
import com.rand.ishowApi.clip.admin.domain.repository.ClipRepository;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminSectionResponse;
import com.rand.ishowApi.movie.admin.application.service.MovieOpenSearchService;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.openSearch.index.MoviesIndex;
import com.rand.ishowApi.movie.admin.domain.repository.MoviesRepository;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
import com.rand.ishowApi.series.admin.api.response.SeriesSectionAdminResponse;
import com.rand.ishowApi.series.admin.application.service.SeriesOpenSearchService;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.series.admin.domain.repository.SeriesRepository;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramSectionAdminResponse;
import com.rand.ishowApi.tvProgram.admin.application.service.TvProgramOpenSearchService;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.index.TvProgramIndex;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SportAdminService {

    private final MovieOpenSearchService movieOpenSearchService;
    private final SeriesOpenSearchService seriesOpenSearchService;
    private final MoviesRepository moviesRepository;
    private final SectionRepository sectionRepository;
    private final SeriesRepository seriesRepository;
    private final ClipRepository clipRepository;
    private final ClipOpenSearchService clipOpenSearchService;
    private final TvProgramRepository tvProgramRepository;
    private final TvProgramOpenSearchService tvProgramOpenSearchService;
    private final ChannelOpenSearchService channelOpenSearchService;
    private final ChannelRepository channelRepository;

    //MOVIE
    public void addSportMovieSection(String movieId, Long sectionId, String isTop) throws IOException {

        Movies movie = moviesRepository.findById(movieId).orElseThrow(
                ()-> new CustomException(ApiResponseCode.MOVIE_NOT_FOUND,null));

        Section section = sectionRepository.findById(sectionId).orElseThrow(
                ()-> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS,null)
        );


        movieOpenSearchService.addMovieToSection(movie, section, MoviesIndex.MOVIES_SPORT.getIndexName(), isTop);
    }

    public void removeSportMovieSection(String movieId, Long sectionId) {
        movieOpenSearchService.removeMovieFromSection(sectionId, movieId, MoviesIndex.MOVIES_SPORT.getIndexName());
    }
    public void updateSportMovieIsTop(String movieId, Long sectionId, String isTop) throws IOException {
        movieOpenSearchService.updateMovieIsTop(sectionId, movieId, MoviesIndex.MOVIES_SPORT.getIndexName(), isTop);
    }
    public List<MovieAdminSectionResponse> getSportMovieSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return movieOpenSearchService.getMoviesSection(sectionId, MoviesIndex.MOVIES_SPORT.getIndexName(), isTop, page, size);
    }


    // SERIES
    public void addSportSeriesSection(String seriesId, Long sectionId, String isTop) throws IOException {
        Series series = seriesRepository.findById(seriesId).orElseThrow(
                ()-> new CustomException(ApiResponseCode.SERIES_NOT_FOUND,null)
        );
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(()-> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS,null));


        seriesOpenSearchService.addSeriesToSection(series, section, SeriesIndex.SERIES_SPORT.getIndexName(), isTop);
    }
    public void removeSportSeriesSection(String seriesId, Long sectionId) throws IOException {
        seriesOpenSearchService.removeSeriesFromSection(sectionId, seriesId, SeriesIndex.SERIES_SPORT.getIndexName());
    }
    public void updateSportSeriesIsTop(String seriesId, Long sectionId, String isTop) throws IOException {
        seriesOpenSearchService.updateSeriesIsTop(sectionId, seriesId, SeriesIndex.SERIES_SPORT.getIndexName(), isTop);
    }
    public  List<SeriesSectionAdminResponse> getSportSeriesSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return seriesOpenSearchService.getSeriesSection(sectionId, SeriesIndex.SERIES_SPORT.getIndexName(), isTop, page, size);
    }


    //Clips
    public void addSportClipSection(String clipId, Long sectionId, String isTop) throws IOException {
        Clip clip = clipRepository.findById(clipId).orElseThrow(()-> new CustomException(ApiResponseCode.CLIP_NOT_FOUND,null));
        Section section = sectionRepository.findById(sectionId).orElseThrow(
                ()-> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null));

        clipOpenSearchService.addClipToSection(clip, section, ClipsIndex.CLIPS_SPORT.getIndexName(), isTop);
    }
    public void removeSportClipSection(String clipId, Long sectionId) {
        clipOpenSearchService.removeClipFromSection(sectionId, clipId, ClipsIndex.CLIPS_SPORT.getIndexName());
    }
    public void updateSportClipIsTop(String clipId, Long sectionId, String isTop) throws IOException {
        clipOpenSearchService.updateClipIsTop(sectionId, clipId, ClipsIndex.CLIPS_SPORT.getIndexName(), isTop);
    }

    public List<ClipSectionAdminResponse> getSportClipSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return clipOpenSearchService.getClipsSection(sectionId, ClipsIndex.CLIPS_SPORT.getIndexName(), isTop, page, size);
    }


    //TV PROGRAM
    public void addSportTvProgramSection( String programId ,Long sectionId ,String isTop ) throws IOException {
        TvProgram program =tvProgramRepository.findById(programId)
                .orElseThrow(()-> new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND,null));
        Section section =sectionRepository.findById(sectionId).orElseThrow(()-> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS,null));


        tvProgramOpenSearchService.addTvProgramToSection(program,section, TvProgramIndex.TV_PROGRAM_SPORT.getIndexName(), isTop);

    }

    public void removeSportTvProgramSection( String programId ,Long sectionId ) {
        tvProgramOpenSearchService.removeTvProgramFromSection(sectionId, programId, TvProgramIndex.TV_PROGRAM_SPORT.getIndexName());
    }

    public void updateSportTvProgramIsTop( String programId ,Long sectionId ,String isTop ) throws IOException {
        tvProgramOpenSearchService.updateTvProgramIsTop(sectionId, programId, TvProgramIndex.TV_PROGRAM_SPORT.getIndexName(), isTop);
    }

    public List<TvProgramSectionAdminResponse> getSportTvProgramSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return tvProgramOpenSearchService.getTvProgramSection(sectionId, TvProgramIndex.TV_PROGRAM_SPORT.getIndexName(), isTop, page, size);
    }


    //Channel
    public void addSportChannelSection(String channelId, Long sectionId, String isTop) throws IOException {
        Channel channel = channelRepository.findById(channelId).orElseThrow(()-> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND,null));
        Section section = sectionRepository.findById(sectionId).orElseThrow(()-> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS,null));
        channelOpenSearchService.addChannelToSection(channel, section, ChannelIndex.CHANNEL_SPORT.getIndexName(), isTop);
    }

    public void removeSportChannelSection(String channelId, Long sectionId) {
        channelOpenSearchService.removeChannelFromSection(sectionId, channelId, ChannelIndex.CHANNEL_SPORT.getIndexName());
    }

    public void updateSportChannelIsTop(String channelId, Long sectionId, String isTop) throws IOException {
        channelOpenSearchService.updateChannelIsTop(sectionId, channelId, ChannelIndex.CHANNEL_SPORT.getIndexName(), isTop);
    }

    public List<ChannelAdminSectionResponse> getSportChannelSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return channelOpenSearchService.getChannelsSection(sectionId, ChannelIndex.CHANNEL_SPORT.getIndexName(), isTop, page, size);
    }
}
