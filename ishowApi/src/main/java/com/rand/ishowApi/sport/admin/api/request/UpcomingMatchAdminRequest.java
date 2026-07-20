
package com.rand.ishowApi.sport.admin.api.request;

import org.springframework.web.multipart.MultipartFile;

public record UpcomingMatchAdminRequest(
		String id,
		Long homeTeamId,
		Long awayTeamId,
		Long championId,
		String channelId,
		String matchDate,
		String active
){ }


