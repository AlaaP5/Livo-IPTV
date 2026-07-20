package com.rand.ishowApi.channel.mobile.application;

import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.entity.ChannelEpg;
import com.rand.ishowApi.channel.admin.domain.openSearch.index.ChannelIndex;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelEpgRepository;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelRepository;
import com.rand.ishowApi.channel.mobile.api.response.ChannelDetailsResponse;
import com.rand.ishowApi.channel.mobile.api.response.ChannelSectionResponse;
import com.rand.ishowApi.channel.mobile.mapper.ChannelDocMapper;
import com.rand.ishowApi.channel.mobile.mapper.ChannelMobileMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.favorite.application.service.FavoriteService;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.openSearch.service.GenericSectionService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.application.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelMobileService {

	private final GenericSectionService sectionService;
	private final ChannelMobileMapper mobileMapper;
	private final ChannelDocMapper docMapper;
	private final ChannelRepository channelRepository;
	private final ChannelEpgRepository channelEpgRepository;
	private final WatchListService watchListService;
	private final FavoriteService favoriteService;


	public SectionBannerResponse<ChannelSectionResponse> getChannelSections() throws IOException {
		SectionBannerResponse<ChannelSectionResponse> response = sectionService.getSections(
				ChannelIndex.CHANNEL_SECTION.getIndexName(),
				ChannelDocument.class,
				ContentType.CHANNELS,
				docMapper,
				true
		);

		enrichBannerWithCurrentEpg(response);

		return response;
	}

	public List<ChannelSectionResponse> getChannelSectionById(Long sectionId) throws IOException {
		return sectionService.getSectionContent(
				ChannelIndex.CHANNEL_SECTION.getIndexName(),
				ChannelDocument.class,
				docMapper,
				sectionId,
				true
		);
	}

	public ChannelDetailsResponse getChannelDetails(String channelId) {
		Channel channel = channelRepository.findById(channelId)
				.orElseThrow(() -> new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null));

		List<ChannelEpg> epgList = channelEpgRepository
				.findActiveProgramsInRange(
						channelId,
						LocalDateTime.now().minusHours(12),
						LocalDateTime.now().plusHours(12)
				);

		boolean isLke = favoriteService.isInFavorite(channelId, ContentType.CHANNELS);
		boolean isWatchList = watchListService.isInWatchList(channelId, ContentType.CHANNELS);

		return mobileMapper.toChannelDetailsResponse(channel, epgList,  isLke, isWatchList);
	}

	private void enrichBannerWithCurrentEpg(SectionBannerResponse<ChannelSectionResponse> response) {
		if (response == null || response.getBanner() == null || response.getBanner().isEmpty()) {
			return;
		}

		LocalDateTime now = LocalDateTime.now();

		for (ChannelSectionResponse bannerItem : response.getBanner()) {
			if (bannerItem == null || bannerItem.getChannelId() == null) {
				continue;
			}

			channelEpgRepository.findCurrentActiveProgram(bannerItem.getChannelId(), now)
					.ifPresent(epg -> bannerItem.setCurrentProgram(mobileMapper.toChannelEpgResponse(epg)));
		}
	}
}
