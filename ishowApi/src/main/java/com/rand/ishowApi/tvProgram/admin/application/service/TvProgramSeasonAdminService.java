package com.rand.ishowApi.tvProgram.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.tvProgram.admin.api.request.AddTvProgramSeasonAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.request.UpdateTvProgramSeasonAdminRequest;
import com.rand.ishowApi.tvProgram.admin.application.manager.TvProgramSeasonDomainManager;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramRepository;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramSeasonRepository;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TvProgramSeasonAdminService {
    private final TvProgramRepository tvProgramRepository;
    private final TvProgramSeasonRepository tvProgramSeasonRepository;
    private final TvProgramSeasonDomainManager tvProgramSeasonDomainManager;
    private final UploadServiceAsync uploadServiceAsync;

    public TvProgramSeason addTvProgramSeason(AddTvProgramSeasonAdminRequest request) {
        TvProgram tvProgram = findTvProgramEntityById(request.getTvProgramId());

        OriginalUploadMetadata posterMetadata = null;
        if (request.getPoster() != null) {
            CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadTvProgramSeasonPosterAsync(
                    request.getPoster(),
                    tvProgram.getId(),
                    request.getSeasonNumber()
            );
            CompletableFuture.allOf(posterFuture).join();
            posterMetadata = posterFuture.join();
        }

        TvProgramSeason season = tvProgramSeasonDomainManager.createSeason(request, posterMetadata);
        tvProgramSeasonRepository.save(season);

        if (request.getTrailer() != null) {
            CompletableFuture<OriginalUploadMetadata> trailerFuture = uploadServiceAsync.uploadTvProgramSeasonPosterAsync(
                    request.getTrailer(),
                    tvProgram.getId(),
                    season.getSeasonNumber()
            );
            CompletableFuture.allOf(trailerFuture).join();
            OriginalUploadMetadata trailerMetadata = trailerFuture.join();
            if (trailerMetadata != null) {
                tvProgramSeasonDomainManager.addSeasonFiles(season, trailerMetadata);
            }
        }

        tvProgramSeasonRepository.save(season);

        Integer seasonCount = tvProgramSeasonRepository.countByTvProgramIdAndActiveTrue(tvProgram.getId());
        tvProgramSeasonDomainManager.updateTvProgramSeasonCount(tvProgram, seasonCount);
        tvProgramRepository.save(tvProgram);

        return season;
    }

    public TvProgramSeason updateTvProgramSeason(UpdateTvProgramSeasonAdminRequest request) {
        TvProgram tvProgram = findTvProgramEntityById(request.getTvProgramId());
        TvProgramSeason season = tvProgramSeasonRepository.findById(request.getSeasonId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_SEASON_NOT_FOUND, null));

        Integer targetSeasonNumber = request.getSeasonNumber() != null
                ? request.getSeasonNumber()
                : season.getSeasonNumber();

        OriginalUploadMetadata posterMetadata = null;
        if (request.getPoster() != null) {
            CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadTvProgramSeasonPosterAsync(
                    request.getPoster(),
                    tvProgram.getId(),
                    targetSeasonNumber
            );
            CompletableFuture.allOf(posterFuture).join();
            posterMetadata = posterFuture.join();

            if (season.getPoster() != null) {
                uploadServiceAsync.removeOldFileAsync(season.getPoster());
            }
        }

        tvProgramSeasonDomainManager.updateSeason(season, request, posterMetadata);

        if (request.getTrailer() != null) {
            CompletableFuture<OriginalUploadMetadata> trailerFuture = uploadServiceAsync.uploadTvProgramSeasonPosterAsync(
                    request.getTrailer(),
                    tvProgram.getId(),
                    season.getSeasonNumber()
            );
            CompletableFuture.allOf(trailerFuture).join();
            OriginalUploadMetadata trailerMetadata = trailerFuture.join();

            if (season.getTrailer() != null && season.getTrailer().getOriginalUploadMetadata() != null) {
                uploadServiceAsync.removeOldFileAsync(season.getTrailer().getOriginalUploadMetadata());
            }

            if (trailerMetadata != null) {
                tvProgramSeasonDomainManager.addSeasonFiles(season, trailerMetadata);
            }
        }

        tvProgramSeasonRepository.save(season);

        Integer seasonCount = tvProgramSeasonRepository.countByTvProgramIdAndActiveTrue(tvProgram.getId());
        tvProgramSeasonDomainManager.updateTvProgramSeasonCount(tvProgram, seasonCount);
        tvProgramRepository.save(tvProgram);

        return season;
    }

    public List<TvProgramSeason> findTvProgramSeasonByTvProgramId(String tvProgramId) {
        List<TvProgramSeason> seasons = tvProgramSeasonRepository.findByTvProgramId(tvProgramId);
        if (seasons == null) {
            return List.of();
        }

        return seasons;
    }

    private TvProgram findTvProgramEntityById(String id) {
        return tvProgramRepository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND, null));
    }
}


