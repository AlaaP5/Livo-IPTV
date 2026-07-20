package com.rand.ishowApi.openSearch.resolver;

import com.rand.ishowApi.channel.admin.domain.openSearch.index.ChannelIndex;
import com.rand.ishowApi.clip.admin.domain.openSearch.index.ClipsIndex;
import com.rand.ishowApi.movie.admin.domain.openSearch.index.MoviesIndex;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.index.TvProgramIndex;
import org.springframework.stereotype.Component;

@Component
public class SectionIndexResolver {

    public String resolve(Section section) {

        ContentType contentType = section.getContentType();
        MobilePage page = section.getPage();

        return switch (contentType) {

            case CLIPS ->
                    ClipsIndex.getIndexNameByPage(page);

            case MOVIES ->
                    MoviesIndex.getIndexNameByPage(page);

            case SERIES ->
                    SeriesIndex.getIndexNameByPage(page);

            case TV_PROGRAMS ->
                    TvProgramIndex.getIndexNameByPage(page);

            case CHANNELS ->
                    ChannelIndex.getIndexNameByPage(page);

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported content type: " + contentType
                    );
        };
    }
}