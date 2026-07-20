package com.rand.ishowApi.tag.admin.application.service;


import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.tag.admin.api.request.TagAdminRequest;
import com.rand.ishowApi.tag.admin.api.request.TagFilterAdminRequest;
import com.rand.ishowApi.tag.admin.api.response.TagAdminResponse;
import com.rand.ishowApi.tag.admin.application.manager.TagDomainManager;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.admin.domain.repository.TagRepository;
import com.rand.ishowApi.tag.admin.domain.repository.TagSpecification;
import com.rand.ishowApi.tag.admin.mapper.TagAdminMapper;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagAdminService {

    private final TagDomainManager manager;
    private final TagAdminMapper mapper;
    private final TagRepository repository;

    public TagAdminResponse createTag(TagAdminRequest request) {

        validateEventTitleExistsOnCreate(request);

        Tag tag = manager.create(request);

        repository.save(tag);

        return mapper.toResponse(tag);
    }

    public TagAdminResponse updateTag(TagAdminRequest request) {

            Tag tag = repository.findById(request.getId())
                    .orElseThrow(() -> new CustomException(ApiResponseCode.TAG_NOT_EXISTS, null));

        validateEventTitleExistsOnUpdate(request);
        manager.update(tag, request);

        repository.save(tag);

        return mapper.toResponse(tag);
    }


    public TagAdminResponse activateTag(Long id, String active) {

        Tag tag = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.TAG_NOT_EXISTS, null));

        manager.setActive(tag, active);

        repository.save(tag);

        return mapper.toResponse(tag);
    }

    public TagAdminResponse activateCommon(Long id, String common) {

        Tag tag = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.TAG_NOT_EXISTS, null));

        manager.setCommon(tag, common);

        repository.save(tag);

        return mapper.toResponse(tag);
    }

    public Page<TagAdminResponse> filterTag(TagFilterAdminRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage() -1,
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );


        Specification<Tag> specification = Specification
                .where(TagSpecification.hasActive(BooleanConverter.getActiveBoolean(request.getActive())))
                .and(TagSpecification.hasTitle(request.getTitle()))
                .and(TagSpecification.hasCommon(BooleanConverter.getActiveBoolean(request.getCommon())));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }


    // ========= validation ============

    private void validateEventTitleExistsOnCreate(TagAdminRequest request) {
        boolean isDuplicate = repository.existsByTitleEnIgnoreCaseOrTitleArIgnoreCase(
                request.getTitleEn(),
                request.getTitleAr()
        );

        if (isDuplicate) {
            throw new CustomException(ApiResponseCode.TAG_TITLE_EXISTS, null);
        }
    }

    private void validateEventTitleExistsOnUpdate(TagAdminRequest request) {
        boolean isDuplicate = repository.existsByIdNotAndTitleEnIgnoreCaseOrIdNotAndTitleArIgnoreCase(
                request.getId(),
                request.getTitleEn(),
                request.getId(),
                request.getTitleAr()
        );

        if (isDuplicate) {
            throw new CustomException(ApiResponseCode.TAG_TITLE_EXISTS, null);
        }
    }
}
