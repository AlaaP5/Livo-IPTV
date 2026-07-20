package com.rand.ishowApi.kids.admin.application.manager;


import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.kids.admin.domain.model.KidsBanner;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class KidsDomainManager {


    public KidsBanner mapMovie(Movies movie){
      KidsBanner banner = new KidsBanner();
      banner.setContentId(movie.getId());
      banner.setContentArabicTitle(movie.getTitleAr());
      banner.setContentEnglishTitle(movie.getTitleEn());
      banner.setContentType(ContentType.MOVIES);
      banner.setPoster(movie.getPoster().getGeneratedPath());
      banner.setTagMeta(movie.getTags());
      banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
      banner.setIsKids(true);

      return banner;
    }

    public KidsBanner mapSeries(Series series){
        KidsBanner banner = new KidsBanner();
        banner.setContentId(series.getId());
        banner.setContentArabicTitle(series.getTitleAr());
        banner.setContentEnglishTitle(series.getTitleEn());
        banner.setContentType(ContentType.SERIES);
        banner.setPoster(series.getPoster().getGeneratedPath());
        banner.setTagMeta(series.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        banner.setIsKids(true);
        return banner;
    }

    public KidsBanner mapTvProgram(TvProgram program){
        KidsBanner banner = new KidsBanner();
        banner.setContentId(program.getId());
        banner.setContentArabicTitle(program.getTitleAr());
        banner.setContentEnglishTitle(program.getTitleEn());
        banner.setContentType(ContentType.TV_PROGRAMS);
        banner.setPoster(program.getPoster().getGeneratedPath());
        banner.setTagMeta(program.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        banner.setIsKids(true);
        return banner;
    }

    public KidsBanner mapChannel(Channel channel){
        KidsBanner banner = new KidsBanner();
        banner.setContentId(channel.getId());
        banner.setContentArabicTitle(channel.getTitleAr());
        banner.setContentEnglishTitle(channel.getTitleEn());
        banner.setContentType(ContentType.CHANNELS);
        banner.setPoster(channel.getLogo().getGeneratedPath());
        banner.setTagMeta(channel.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        banner.setIsKids(true);
        return banner;
    }

    public KidsBanner mapClip(Clip clip){
        KidsBanner banner = new KidsBanner();
        banner.setContentId(clip.getId());
        banner.setContentArabicTitle(clip.getTitleAr());
        banner.setContentEnglishTitle(clip.getTitleEn());
        banner.setContentType(ContentType.CLIPS);
        banner.setPoster(clip.getPoster().getGeneratedPath());
        banner.setTagMeta(clip.getTags());
        banner.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        banner.setIsKids(true);
        return banner;
    }


}

