package com.rand.ishowApi.actor.admin.api.request;

import org.springframework.web.multipart.MultipartFile;

public record ActorAdminRequest(Long id, String nameEn, String nameAr, MultipartFile imagePath, String active)
{}
