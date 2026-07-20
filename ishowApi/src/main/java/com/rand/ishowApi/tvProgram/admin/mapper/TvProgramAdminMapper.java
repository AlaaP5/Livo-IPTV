package com.rand.ishowApi.tvProgram.admin.mapper;

import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramAdminResponse;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramSectionAdminResponse;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TvProgramAdminMapper {

	public List<TvProgramAdminResponse> toFilterResponse(List<TvProgram> list) {
		return list.stream().map(this::toFilterResponseItem).toList();
	}

	private TvProgramAdminResponse toFilterResponseItem(TvProgram tv) {
		TvProgramAdminResponse response = new TvProgramAdminResponse();
		response.setTvProgramId(tv.getId());
		response.setTitleEn(tv.getTitleEn());
		response.setTitleAr(tv.getTitleAr());
		response.setDescriptionEn(tv.getDescriptionEn());
		response.setDescriptionAr(tv.getDescriptionAr());
		response.setPoster(tv.getPoster());
		response.setBadge(tv.getBadge());
		response.setTags(tv.getTags());
		response.setLanguage(tv.getLanguage());
		response.setHasRight(tv.getHasRight());
		response.setIsKids(tv.getIsKids());
		response.setActive(tv.getActive());
		response.setIsSports(tv.getIsSports());
		response.setReleaseYear(tv.getReleaseYear());
		response.setSubtitleLanguages(tv.getSubtitleLanguages());
 		response.setAudioLanguages(tv.getAudioLanguages());
		response.setIsPublish(tv.getIsPublish());
		return response;
	}

	public TvProgramSectionAdminResponse toTvProgramSectionResponse(TvProgramDocument document) {
		if (document == null) {
			return null;
		}

		TvProgramSectionAdminResponse response = new TvProgramSectionAdminResponse();
		response.setTvProgramId(document.getTvProgramId());
		response.setTitleEn(document.getTitleEn());
		response.setTitleAr(document.getTitleAr());
		response.setDescriptionEn(document.getDescriptionEn());
		response.setDescriptionAr(document.getDescriptionAr());
		response.setPoster(document.getPoster());
		response.setBadge(document.getBadge());
		response.setTags(document.getTags());
		response.setReleaseYear(document.getReleaseYear());
		response.setIsPublish(document.getIsPublish());
		response.setHasRight(document.getHasRight());
		response.setLanguage(document.getLanguage());
		response.setActive(document.getActive());
		response.setIsKids(document.getIsKids());
		response.setIsSports(document.getIsSports());
		response.setIsTop(document.getIsTop());
		response.setCreateDate(document.getCreateDate());
		response.setSeasonCount(document.getSeasonCount());
		response.setSubtitleLanguages(document.getSubtitleLanguages());
		response.setAudioLanguages(document.getAudioLanguages());

		response.setSectionId(document.getSectionId());
		response.setSectionTitleAr(document.getSectionTitleAr());
		response.setSectionTitleEn(document.getSectionTitleEn());
		response.setSectionOrder(document.getSectionOrder());
		response.setSectionActive(document.isSectionActive());
		response.setSectionPublish(document.isSectionPublish());

		return response;
	}

	public List<TvProgramSectionAdminResponse> toTvProgramSectionResponseList(List<TvProgramDocument> documents) {
		return documents.stream()
				.map(this::toTvProgramSectionResponse)
				.collect(Collectors.toList());
	}

}



