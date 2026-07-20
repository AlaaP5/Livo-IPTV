package com.rand.ishowApi.tvProgram.admin.api.request;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateTvProgramAdminRequest {
	private String tvProgramId;
	private String titleEn;
	private String titleAr;
	private String descriptionEn;
	private String descriptionAr;
	private Badge badge;
	private List<Long> tags;
	private ContentLanguage language;
	private String hasRight;
	private String isKids;
	private String active;
	private String isSports;
	private Integer releaseYear;
	private java.util.List<ContentLanguage> subtitleLanguages;
	private java.util.List<ContentLanguage> audioLanguages;
	private MultipartFile poster;
}


