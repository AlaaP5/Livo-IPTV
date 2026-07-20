package com.rand.ishowApi.favorite.mapper;

import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.repository.ChannelRepository;
import com.rand.ishowApi.channel.mobile.api.response.ChannelSectionResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChannelFavoriteAdapter implements FavoriteContentAdapter<Channel, ChannelSectionResponse> {
    private final ChannelRepository channelRepository;

    @Override
    public ContentType contentType() {
        return ContentType.CHANNELS;
    }

    @Override
    public String notFoundCode() {
        return ApiResponseCode.CHANNEL_NOT_FOUND;
    }

    @Override
    public Optional<Channel> findById(String contentId) {
        if (contentId == null) {
            return Optional.empty();
        }
        return channelRepository.findById(contentId);
    }

    @Override
    public Map<String, Channel> findAllByIds(Collection<String> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Channel> channels = new ArrayList<>(channelRepository.findAllById(contentIds));
        return channels.stream().collect(Collectors.toMap(
                Channel::getId,
                Function.identity(),
                (left, right) -> left,
                LinkedHashMap::new
        ));
    }

    @Override
    public ChannelSectionResponse toResponse(Channel channel, String lang) {
        ChannelSectionResponse response = new ChannelSectionResponse();
        response.setChannelId(channel.getId());
        response.setTitle(LocalizedText.getName(channel.getTitleEn(), channel.getTitleAr(), lang));
        response.setBadge(channel.getBadge());
        if (channel.getLogo() != null) {
            response.setLogo(channel.getLogo().getGeneratedPath());
        }


        if (channel.getTags() != null) {
            response.setTags( channel.getTags().stream()
                    .map(tag -> LocalizedText.getName(
                            tag.getTitleEn(),
                            tag.getTitleAr(),
                            lang
                    ))
                    .collect(Collectors.toList()));

        } else {
            response.setTags(Collections.emptyList());
        }
        return response;
    }
}


