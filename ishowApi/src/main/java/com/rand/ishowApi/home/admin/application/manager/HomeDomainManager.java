package com.rand.ishowApi.home.admin.application.manager;


import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.home.admin.domain.model.HomeBanner;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class HomeDomainManager {


    public HomeBanner mapMovie(Movies movie){
      HomeBanner banner = new HomeBanner();
      banner.setContentId(movie.getId());
      banner.setContentArabicTitle(movie.getTitleAr());
      banner.setContentEnglishTitle(movie.getTitleEn());
      banner.setContentType(ContentType.MOVIES);
      banner.setPoster(movie.getPoster().getGeneratedPath());
      banner.setTagMeta(movie.getTags());
      banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));

      return banner;
    }

    public HomeBanner mapSeries(Series series){
        HomeBanner banner = new HomeBanner();
        banner.setContentId(series.getId());
        banner.setContentArabicTitle(series.getTitleAr());
        banner.setContentEnglishTitle(series.getTitleEn());
        banner.setContentType(ContentType.SERIES);
        banner.setPoster(series.getPoster().getGeneratedPath());
        banner.setTagMeta(series.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        return banner;
    }

    public HomeBanner mapTvProgram(TvProgram program){
        HomeBanner banner = new HomeBanner();
        banner.setContentId(program.getId());
        banner.setContentArabicTitle(program.getTitleAr());
        banner.setContentEnglishTitle(program.getTitleEn());
        banner.setContentType(ContentType.TV_PROGRAMS);
        banner.setPoster(program.getPoster().getGeneratedPath());
        banner.setTagMeta(program.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        return banner;
    }

    public HomeBanner mapChannel(Channel channel){
        HomeBanner banner = new HomeBanner();
        banner.setContentId(channel.getId());
        banner.setContentArabicTitle(channel.getTitleAr());
        banner.setContentEnglishTitle(channel.getTitleEn());
        banner.setContentType(ContentType.CHANNELS);
        banner.setPoster(channel.getLogo().getGeneratedPath());
        banner.setTagMeta(channel.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        return banner;
    }

    public HomeBanner mapClip(Clip clip){
        HomeBanner banner = new HomeBanner();
        banner.setContentId(clip.getId());
        banner.setContentArabicTitle(clip.getTitleAr());
        banner.setContentEnglishTitle(clip.getTitleEn());
        banner.setContentType(ContentType.CLIPS);
        banner.setPoster(clip.getPoster().getGeneratedPath());
        banner.setTagMeta(clip.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        return banner;
    }




}
