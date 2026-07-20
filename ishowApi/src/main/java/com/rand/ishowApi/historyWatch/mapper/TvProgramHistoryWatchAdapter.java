package com.rand.ishowApi.historyWatch.mapper;

import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramRepository;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSectionResponse;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TvProgramHistoryWatchAdapter implements HistoryWatchContentAdapter<TvProgram, TvProgramSectionResponse> {
    private final TvProgramRepository tvProgramRepository;

    @Override
    public ContentType contentType() {
        return ContentType.TV_PROGRAMS;
    }

    @Override
    public String notFoundCode() {
        return ApiResponseCode.TV_PROGRAM_NOT_FOUND;
    }

    @Override
    public Optional<TvProgram> findById(String contentId) {
        if (contentId == null) {
            return Optional.empty();
        }
        return tvProgramRepository.findById(contentId);
    }

    @Override
    public Map<String, TvProgram> findAllByIds(Collection<String> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<TvProgram> tvPrograms = new ArrayList<>(tvProgramRepository.findAllById(contentIds));
        return tvPrograms.stream().collect(Collectors.toMap(
                TvProgram::getId,
                Function.identity(),
                (left, right) -> left,
                LinkedHashMap::new
        ));
    }

    @Override
    public TvProgramSectionResponse toResponse(TvProgram tvProgram, String lang) {
        TvProgramSectionResponse response = new TvProgramSectionResponse();
        response.setTvProgramId(tvProgram.getId());
        response.setTitle(LocalizedText.getName(tvProgram.getTitleEn(), tvProgram.getTitleAr(), lang));
        response.setBadge(tvProgram.getBadge());
        response.setReleaseYear(tvProgram.getReleaseYear());
        response.setSeasonCount(tvProgram.getSeasonCount());
        if (tvProgram.getPoster() != null) {
            response.setPoster(tvProgram.getPoster().getGeneratedPath());
        }
        response.setTags(tvProgram.getTags() == null ? Collections.emptyList() : tvProgram.getTags().stream()
                .map(tag -> mapTag(tag, lang))
                .toList());
        return response;
    }

    private String mapTag(TagMeta tag, String lang) {
        return tag == null ? null : LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang);
    }
}

