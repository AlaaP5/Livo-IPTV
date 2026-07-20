package com.rand.ishowApi.ads.admin.application.service;

import com.rand.ishowApi.ads.admin.api.request.AdAdminFilterRequest;
import com.rand.ishowApi.ads.admin.api.request.AdAdminRequest;
import com.rand.ishowApi.ads.admin.api.response.AdAdminResponse;
import com.rand.ishowApi.ads.admin.application.manager.AdDomainManger;
import com.rand.ishowApi.ads.domain.entity.Ad;
import com.rand.ishowApi.ads.domain.repository.AdRepository;
import com.rand.ishowApi.ads.admin.mapper.AdAdminMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import static com.rand.ishowApi.ads.domain.repository.AdSpecification.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AdAdminService {

    private final AdDomainManger manager;
    private final AdAdminMapper mapper;
    private final AdRepository repository;
    private final UploadServiceAsync uploadServiceAsync;

    public AdAdminResponse createAd(AdAdminRequest request) {

        LocalDate today = LocalDate.now();

        if (request.startDate().isBefore(today)) {
            throw new CustomException(ApiResponseCode.AD_INVALID_DATE_RANGE, null);
        }

        if (!request.endDate().isAfter(request.startDate())) {
            throw new CustomException(ApiResponseCode.AD_INVALID_DATE_RANGE, null);
        }

        Ad ad = manager.createAd(request);

        repository.save(ad);

        CompletableFuture<OriginalUploadMetadata> posterFuture =
                uploadServiceAsync.uploadAdPosterAsync(request.imagePath(), ad.getId().toString());

        manager.addAdPoster(ad, posterFuture.join());

        Ad savedAd = repository.save(ad);

        return mapper.toResponse(savedAd);
    }

    public AdAdminResponse updateAd(AdAdminRequest request) {

        Ad ad = repository.findById(request.id())
                .orElseThrow(() -> new CustomException(ApiResponseCode.AD_NOT_EXISTS, null));

        LocalDate today = LocalDate.now();

        if (request.startDate().isBefore(today)) {
            throw new CustomException(ApiResponseCode.AD_INVALID_DATE_RANGE, null);
        }

        if (!request.endDate().isAfter(request.startDate())) {
            throw new CustomException(ApiResponseCode.AD_INVALID_DATE_RANGE, null);
        }

        CompletableFuture<OriginalUploadMetadata> posterFuture = null;

        if (request.imagePath() != null) {
            posterFuture = uploadServiceAsync.uploadAdPosterAsync(request.imagePath(), ad.getId().toString());
        }

        OriginalUploadMetadata posterMetadata = posterFuture != null ? posterFuture.join() : null;

        if (posterMetadata != null || (request.imagePath() != null)) {
            if (ad.getGeneratedImagePath() != null) {
                uploadServiceAsync.removeOldFileAsync(ad.getGeneratedImagePath());
            }
        }

        manager.updateAd(request, ad, posterMetadata);

        repository.save(ad);

        return mapper.toResponse(ad);
    }

    public AdAdminResponse activateAd(Long id, String active) {

        Ad ad = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.AD_NOT_EXISTS, null));

        manager.activateAd(ad, active);

        repository.save(ad);

        return mapper.toResponse(ad);
    }

    public Page<AdAdminResponse> filterAd(AdAdminFilterRequest request){

        Pageable pageable = PageRequest.of(
                request.getPage() -1,
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Specification<Ad> specification = Specification
                .where(hasActive(BooleanConverter.getActiveBoolean(request.getActive())))
                .and(hasBetweenDates(request.getStartDate(), request.getEndDate()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }
}
