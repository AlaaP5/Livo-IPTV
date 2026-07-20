package com.rand.ishowApi.tag.mobile.application.service;

import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.admin.domain.repository.TagRepository;
import com.rand.ishowApi.tag.mobile.api.response.TagMobileResponse;
import com.rand.ishowApi.tag.mobile.mapper.TagMobileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagMobileService {

    private final TagRepository tagRepository;
    private final TagMobileMapper tagMobileMapper;

    public List<TagMobileResponse> getCommonTags() {
        List<Tag> tags = tagRepository.findAllByCommonTrueAndActiveTrueOrderByIdAsc();

        return tags.stream()
                .map(tagMobileMapper::toResponse)
                .toList();
    }
}

