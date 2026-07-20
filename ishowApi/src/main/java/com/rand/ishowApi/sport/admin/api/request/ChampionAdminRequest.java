package com.rand.ishowApi.sport.admin.api.request;

import org.springframework.web.multipart.MultipartFile;

public record ChampionAdminRequest(Long id, String nameEn, String nameAr, MultipartFile imagePath, String active)
{}

