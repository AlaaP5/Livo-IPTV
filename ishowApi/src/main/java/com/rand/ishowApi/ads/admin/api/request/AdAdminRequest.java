package com.rand.ishowApi.ads.admin.api.request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record AdAdminRequest(Long id, MultipartFile imagePath, String deepLink, LocalDate startDate, LocalDate endDate, String active) {}
