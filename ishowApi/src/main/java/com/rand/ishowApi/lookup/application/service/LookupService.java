package com.rand.ishowApi.lookup.application.service;


import com.rand.ishowApi.account.domain.entity.AccountRole;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import com.rand.ishowApi.account.domain.repository.AccountRepository;
import com.rand.ishowApi.actor.domain.entity.Actor;
import com.rand.ishowApi.actor.domain.repository.ActorRepository;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelRepository;
import com.rand.ishowApi.clip.admin.domain.repository.ClipRepository;
import com.rand.ishowApi.lookup.api.response.LookupEnumResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.lookup.api.response.MongoLookupResponse;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.repository.MoviesRepository;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.repository.SeriesRepository;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.admin.domain.repository.TagRepository;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramRepository;
import com.rand.ishowApi.sport.admin.domain.repository.TeamRepository;
import com.rand.ishowApi.sport.admin.domain.entity.Team;
import com.rand.ishowApi.sport.admin.domain.repository.ChampionRepository;
import com.rand.ishowApi.sport.admin.domain.entity.Champion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LookupService {

    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;
    private final ActorRepository actorRepository;
    private final SectionRepository sectionRepository;
    private final MoviesRepository movieRepository;
    private final ClipRepository clipRepository;
    private final SeriesRepository seriesRepository;
    private final TvProgramRepository tvProgramRepository;
    private final ChannelRepository channelRepository;
    private final TeamRepository teamRepository;
    private final ChampionRepository championRepository;


    // ============================
    public List<LookupEnumResponse> getMobilePage() {
        return Arrays.stream(MobilePage.values())
                .map(page -> {
                    LookupEnumResponse response = new LookupEnumResponse();
                    response.setCode(page.name());
                    response.setName(page.getValue());
                    return response;
                })
                .toList();
    }

    // ============================
    public List<LookupEnumResponse> getContentType() {
        return Arrays.stream(ContentType.values())
                .map(contentType -> {
                    LookupEnumResponse response = new LookupEnumResponse();
                    response.setCode(contentType.name());
                    response.setName(contentType.getValue());
                    return response;
                })
                .toList();
    }


    public List<LookupResponse> getAccounts(String name, String lang) {
        return accountRepository.findAll().stream()
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .filter(account -> account.getRole() == AccountRole.CONTENT)
                .filter(account -> name == null || account.getUsername().toLowerCase().contains(name.toLowerCase()))
                .map(account -> {
                    LookupResponse response = new LookupResponse();
                    response.setId(account.getId());
                    response.setName(account.getUsername());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<LookupResponse> getTags(String name, String lang) {
        return tagRepository.findAll().stream()
                .filter(Tag::isActive)
                .filter(tag -> name == null || tag.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(tag -> {
                    LookupResponse response = new LookupResponse();
                    response.setId(tag.getId());
                    response.setName(tag.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<LookupResponse> getActors(String name, String lang) {
        return actorRepository.findAll().stream()
                .filter(Actor::isActive)
                .filter(actor -> name == null || actor.getNameEn().toLowerCase().contains(name.toLowerCase()))
                .map(actor -> {
                    LookupResponse response = new LookupResponse();
                    response.setId(actor.getId());
                    response.setName(actor.getNameEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<LookupResponse> getSections(String name, String lang) {
        return sectionRepository.findAll().stream()
                .filter(Section::getActive)
                .filter(Section::getPublish)
                .filter(section -> name == null || section.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(section -> {
                    LookupResponse response = new LookupResponse();
                    response.setId(section.getId());
                    response.setName(section.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<MongoLookupResponse> getMovies(String name, String lang) {
        return movieRepository.findAll().stream()
                .filter(Movies::getActive)
                .filter(Movies::getIsPublish)
                .filter(movie -> name == null || movie.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(movie -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(movie.getId());
                    response.setName(movie.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<MongoLookupResponse> getClips(String name, String lang) {
        return clipRepository.findAll().stream()
                .filter(Clip::getActive)
                .filter(Clip::getIsPublish)
                .filter(clip -> name == null || clip.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(clip -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(clip.getId());
                    response.setName(clip.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<MongoLookupResponse> getSeries(String name, String lang) {
        return seriesRepository.findAll().stream()
                .filter(Series::getActive)
                .filter(Series::getIsPublish)
                .filter(series -> name == null || series.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(series -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(series.getId());
                    response.setName(series.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getTvPrograms(String name, String lang) {
        return tvProgramRepository.findAll().stream()
                .filter(TvProgram::getActive)
                .filter(TvProgram::getIsPublish)
                .filter(tvProgram -> name == null || tvProgram.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(tvProgram -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(tvProgram.getId());
                    response.setName(tvProgram.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getChannels(String name, String lang) {
        return channelRepository.findAll().stream()
                .filter(Channel::getActive)
                .filter(Channel::getIsPublish)
                .filter(channel -> name == null || channel.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(channel -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(channel.getId());
                    response.setName(channel.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LookupResponse> getTeams(String name, String lang) {
        return teamRepository.findAll().stream()
                .filter(Team::isActive)
                .filter(team -> name == null || team.getNameEn().toLowerCase().contains(name.toLowerCase()))
                .map(team -> {
                    LookupResponse response = new LookupResponse();
                    response.setId(team.getId());
                    response.setName(team.getNameEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LookupResponse> getChampions(String name, String lang) {
        return championRepository.findAll().stream()
                .filter(Champion::isActive)
                .filter(champion -> name == null || champion.getNameEn().toLowerCase().contains(name.toLowerCase()))
                .map(champion -> {
                    LookupResponse response = new LookupResponse();
                    response.setId(champion.getId());
                    response.setName(champion.getNameEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getKidsMovies(String name, String lang) {
        return movieRepository.findAll().stream()
                .filter(Movies::getActive)
                .filter(Movies::getIsPublish)
                .filter(Movies::getIsKids)
                .filter(movie -> name == null || movie.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(movie -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(movie.getId());
                    response.setName(movie.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getSportMovies(String name, String lang) {
        return movieRepository.findAll().stream()
                .filter(Movies::getActive)
                .filter(Movies::getIsPublish)
                .filter(Movies::getIsSports)
                .filter(movie -> name == null || movie.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(movie -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(movie.getId());
                    response.setName(movie.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<MongoLookupResponse> getKidsChannels(String name, String lang) {
        return channelRepository.findAll().stream()
                .filter(Channel::getActive)
                .filter(Channel::getIsPublish)
                .filter(Channel::getIsKids)
                .filter(channel -> name == null || channel.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(channel -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(channel.getId());
                    response.setName(channel.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getSportChannels(String name, String lang) {
        return channelRepository.findAll().stream()
                .filter(Channel::getActive)
                .filter(Channel::getIsPublish)
                .filter(Channel::getIsSports)
                .filter(channel -> name == null || channel.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(channel -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(channel.getId());
                    response.setName(channel.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<MongoLookupResponse> getKidsClips(String name, String lang) {
        return clipRepository.findAll().stream()
                .filter(Clip::getActive)
                .filter(Clip::getIsPublish)
                .filter(Clip::getIsKids)
                .filter(clip -> name == null || clip.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(clip -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(clip.getId());
                    response.setName(clip.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getSportClips(String name, String lang) {
        return clipRepository.findAll().stream()
                .filter(Clip::getActive)
                .filter(Clip::getIsPublish)
                .filter(Clip::getIsSports)
                .filter(clip -> name == null || clip.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(clip -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(clip.getId());
                    response.setName(clip.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public List<MongoLookupResponse> getKidsTvPrograms(String name, String lang) {
        return tvProgramRepository.findAll().stream()
                .filter(TvProgram::getActive)
                .filter(TvProgram::getIsPublish)
                .filter(TvProgram::getIsKids)
                .filter(tvProgram -> name == null || tvProgram.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(tvProgram -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(tvProgram.getId());
                    response.setName(tvProgram.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getSportTvPrograms(String name, String lang) {
        return tvProgramRepository.findAll().stream()
                .filter(TvProgram::getActive)
                .filter(TvProgram::getIsPublish)
                .filter(TvProgram::getIsSports)
                .filter(tvProgram -> name == null || tvProgram.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(tvProgram -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(tvProgram.getId());
                    response.setName(tvProgram.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    // Series methods
    public List<MongoLookupResponse> getKidsSeries(String name, String lang) {
        return seriesRepository.findAll().stream()
                .filter(Series::getActive)
                .filter(Series::getIsPublish)
                .filter(Series::getIsKids)
                .filter(series -> name == null || series.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(series -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(series.getId());
                    response.setName(series.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<MongoLookupResponse> getSportSeries(String name, String lang) {
        return seriesRepository.findAll().stream()
                .filter(Series::getActive)
                .filter(Series::getIsPublish)
                .filter(Series::getIsSports)
                .filter(series -> name == null || series.getTitleEn().toLowerCase().contains(name.toLowerCase()))
                .map(series -> {
                    MongoLookupResponse response = new MongoLookupResponse();
                    response.setId(series.getId());
                    response.setName(series.getTitleEn());
                    return response;
                })
                .collect(Collectors.toList());
    }

}
