package com.rand.ishowApi.channel.mobile.api.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChannelEpgResponse {

	private String id;
	private String title;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

}

