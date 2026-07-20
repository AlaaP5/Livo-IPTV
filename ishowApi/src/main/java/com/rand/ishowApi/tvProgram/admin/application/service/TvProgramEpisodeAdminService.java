package com.rand.ishowApi.tvProgram.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.tvProgram.admin.api.request.AddTvProgramEpisodeAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.request.UpdateTvProgramEpisodeAdminRequest;
import com.rand.ishowApi.tvProgram.admin.application.manager.TvProgramEpisodeDomainManager;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramEpisode;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramEpisodeRepository;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramRepository;
import com.rand.ishowApi.tvProgram.admin.domain.repository.TvProgramSeasonRepository;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TvProgramEpisodeAdminService {
	private final TvProgramRepository tvProgramRepository;
	private final TvProgramSeasonRepository tvProgramSeasonRepository;
	private final TvProgramEpisodeRepository tvProgramEpisodeRepository;
	private final TvProgramEpisodeDomainManager tvProgramEpisodeDomainManager;
	private final UploadServiceAsync uploadServiceAsync;

	public TvProgramEpisode addTvProgramEpisode(AddTvProgramEpisodeAdminRequest request) {
		TvProgram tvProgram = findTvProgramEntityById(request.getTvProgramId());
		TvProgramSeason season = findSeasonById(request.getSeasonId());

		OriginalUploadMetadata posterMetadata = null;
		if (request.getPoster() != null) {
			CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadTvProgramEpisodePosterAsync(
					request.getPoster(),
					tvProgram.getId(),
					season.getSeasonNumber(),
					request.getEpisodeNumber()
			);
			CompletableFuture.allOf(posterFuture).join();
			posterMetadata = posterFuture.join();
		}

		OriginalUploadMetadata originalVideoMetadata = null;
		if (request.getFile() != null) {
			CompletableFuture<OriginalUploadMetadata> originalFuture = uploadServiceAsync.uploadTvProgramEpisodeOriginalVideoAsync(
					request.getFile(),
					tvProgram.getId(),
					season.getSeasonNumber(),
					request.getEpisodeNumber()
			);
			CompletableFuture.allOf(originalFuture).join();
			originalVideoMetadata = originalFuture.join();
		}

		List<OriginalUploadMetadata> subtitles = null;
		if (request.getSubtitles() != null) {
			CompletableFuture<List<OriginalUploadMetadata>> subtitlesFuture = uploadServiceAsync.uploadTvProgramEpisodeSubtitleAsync(
					request.getSubtitles(),
					tvProgram.getId(),
					season.getSeasonNumber(),
					request.getEpisodeNumber()
			);
			CompletableFuture.allOf(subtitlesFuture).join();
			subtitles = subtitlesFuture.join();
		}

		TvProgramEpisode episode = tvProgramEpisodeDomainManager.createEpisode(request, posterMetadata, originalVideoMetadata, subtitles);
		tvProgramEpisodeRepository.save(episode);

		Integer episodeCount = tvProgramEpisodeRepository.countBySeasonIdAndActiveTrueAndIsPublishTrue(season.getId());
		tvProgramEpisodeDomainManager.updateTvProgramEpisodeCount(season, episodeCount);
		tvProgramSeasonRepository.save(season);

		return episode;
	}

	public TvProgramEpisode updateTvProgramEpisode(UpdateTvProgramEpisodeAdminRequest request) {
		TvProgram tvProgram = findTvProgramEntityById(request.getTvProgramId());
		TvProgramSeason season = findSeasonById(request.getSeasonId());
		TvProgramEpisode episode = tvProgramEpisodeRepository.findById(request.getEpisodeId())
				.orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_EPISODE_NOT_FOUND, null));

		Integer targetEpisodeNumber = request.getEpisodeNumber() != null
				? request.getEpisodeNumber()
				: episode.getEpisodeNumber();

		OriginalUploadMetadata posterMetadata = null;
		if (request.getPoster() != null) {
			CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadTvProgramEpisodePosterAsync(
					request.getPoster(),
					tvProgram.getId(),
					season.getSeasonNumber(),
					targetEpisodeNumber
			);
			CompletableFuture.allOf(posterFuture).join();
			posterMetadata = posterFuture.join();

			if (episode.getPoster() != null) {
				uploadServiceAsync.removeOldFileAsync(episode.getPoster());
			}
		}

		OriginalUploadMetadata originalVideoMetadata = null;
		if (request.getFile() != null) {
			CompletableFuture<OriginalUploadMetadata> originalFuture = uploadServiceAsync.uploadTvProgramEpisodeOriginalVideoAsync(
					request.getFile(),
					tvProgram.getId(),
					season.getSeasonNumber(),
					targetEpisodeNumber
			);
			CompletableFuture.allOf(originalFuture).join();
			originalVideoMetadata = originalFuture.join();

			if (episode.getOriginalUploadMetadata() != null) {
				uploadServiceAsync.removeOldFileAsync(episode.getOriginalUploadMetadata());
			}
		}

		List<OriginalUploadMetadata> subtitles = null;
		if (request.getSubtitles() != null) {
			CompletableFuture<List<OriginalUploadMetadata>> subtitlesFuture = uploadServiceAsync.uploadTvProgramEpisodeSubtitleAsync(
					request.getSubtitles(),
					tvProgram.getId(),
					season.getSeasonNumber(),
					targetEpisodeNumber
			);
			CompletableFuture.allOf(subtitlesFuture).join();
			subtitles = subtitlesFuture.join();

			if (episode.getSubtitles() != null) {
				for (OriginalUploadMetadata subtitle : episode.getSubtitles()) {
					uploadServiceAsync.removeOldFileAsync(subtitle);
				}
			}
		}

		tvProgramEpisodeDomainManager.updateEpisode(episode, request, posterMetadata, originalVideoMetadata, subtitles);
		tvProgramEpisodeRepository.save(episode);

		Integer episodeCount = tvProgramEpisodeRepository.countBySeasonIdAndActiveTrueAndIsPublishTrue(season.getId());
		tvProgramEpisodeDomainManager.updateTvProgramEpisodeCount(season, episodeCount);
		tvProgramSeasonRepository.save(season);

		return episode;
	}

	public Page<TvProgramEpisode> findTvProgramEpisodeBySeasonId(String tvProgramId, String seasonId, int page, int size) {
		int pageIndex = Math.max(page, 1) - 1;
		int pageSize = Math.max(size, 1);
		Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.ASC, "episodeNumber"));
		return tvProgramEpisodeRepository.findPageByTvProgramIdAndSeasonId(tvProgramId, seasonId, pageable);
	}

	public void updateTvProgramEpisodeTranscodeResult(String episodeId, TranscodeMetaData metaData) throws IOException {
		TvProgramEpisode episode = tvProgramEpisodeRepository.findById(episodeId)
				.orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_EPISODE_NOT_FOUND, null));

		tvProgramEpisodeDomainManager.addEpisodeTranscodeFile(episode, metaData);
		tvProgramEpisodeRepository.save(episode);
	}

	public TvProgramEpisode publishTvProgramEpisode(String episodeId) throws IOException {
		TvProgramEpisode episode = tvProgramEpisodeRepository.findById(episodeId)
				.orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_EPISODE_NOT_FOUND, null));

		if (episode.getTranscodeStatus() != null && episode.getTranscodeStatus() != TranscodeStatus.COMPLETED) {
			throw new CustomException(ApiResponseCode.TV_PROGRAM_EPISODE_TRANSCODE_NOT_COMPLETE, null);
		}

		tvProgramEpisodeDomainManager.publishEpisode(episode);
		tvProgramEpisodeRepository.save(episode);

		TvProgramSeason season = tvProgramSeasonRepository.findById(episode.getSeasonId())
				.orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_SEASON_NOT_FOUND, null));

		Integer episodeCount = tvProgramEpisodeRepository.countBySeasonIdAndActiveTrueAndIsPublishTrue(season.getId());
		tvProgramEpisodeDomainManager.updateTvProgramEpisodeCount(season, episodeCount);
		tvProgramSeasonRepository.save(season);

		return episode;
	}

	private TvProgram findTvProgramEntityById(String id) {
		return tvProgramRepository.findById(id)
				.orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND, null));
	}

	private TvProgramSeason findSeasonById(String id) {
		return tvProgramSeasonRepository.findById(id)
				.orElseThrow(() -> new CustomException(ApiResponseCode.TV_PROGRAM_SEASON_NOT_FOUND, null));
	}
}

