package com.rand.ishowApi.section.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.openSearch.resolver.SectionIndexResolver;
import com.rand.ishowApi.openSearch.service.GenericSectionService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.admin.api.request.SectionAdminRequest;
import com.rand.ishowApi.section.admin.api.request.SectionFilterAdminRequest;
import com.rand.ishowApi.section.admin.api.response.SectionAdminResponse;
import com.rand.ishowApi.section.admin.application.manager.SectionDomainManager;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
import com.rand.ishowApi.section.domain.repository.SectionSpecification;
import com.rand.ishowApi.section.admin.mapper.SectionAdminMapper;
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
public class SectionAdminService {

    private final SectionDomainManager manager;
    private final SectionAdminMapper mapper;
    private final SectionRepository repository;
    private final GenericSectionService sectionService;
    private final SectionIndexResolver sectionIndexResolver;

    public SectionAdminResponse createSection(SectionAdminRequest request) {
        validateSectionTitleExistsOnCreate(request);

        Section section = manager.create(request);
        repository.save(section);
        return mapper.toResponse(section);
    }

    public SectionAdminResponse updateSection(SectionAdminRequest request) {
        Section section = repository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null));

        validateSectionTitleExistsOnUpdate(request);
        manager.update(section, request);
        repository.save(section);

        try {
            sectionService.updateSectionData(sectionIndexResolver.resolve(section), section);
        } catch (Exception ignored) {}

        return mapper.toResponse(section);
    }

    public SectionAdminResponse activateSection(Long id, String active) {
        Section section = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null));

        manager.setActive(section, active);
        repository.save(section);

        try {
            sectionService.updateSectionData(sectionIndexResolver.resolve(section), section);
        } catch (Exception ignored) {}

        return mapper.toResponse(section);
    }

    public SectionAdminResponse publishSection(Long id, String publish) {
        Section section = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null));

        if(!section.getActive()){
            throw new CustomException(ApiResponseCode.SECTION_MUST_ACTIVE, null);
        }
        manager.setPublish(section, publish);
        repository.save(section);
        return mapper.toResponse(section);
    }

    public Page<SectionAdminResponse> filterSections(SectionFilterAdminRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Specification<Section> specification = Specification
                .where(SectionSpecification.hasActive(BooleanConverter.getActiveBoolean(request.getActive())))
                .and(SectionSpecification.hasPublish(BooleanConverter.getActiveBoolean(request.getPublish())))
                .and(SectionSpecification.hasTitle(request.getTitle()))
                .and(SectionSpecification.hasPage(request.getSectionPage()))
                .and(SectionSpecification.hasContentType(request.getContentType()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }

    private void validateSectionTitleExistsOnCreate(SectionAdminRequest request) {
        boolean isDuplicate = repository.existsByTitleEnIgnoreCaseOrTitleArIgnoreCase(
                request.getTitleEn(),
                request.getTitleAr(),
                request.getPage(),
                request.getContentType()
        );

        if (isDuplicate) {
            throw new CustomException(ApiResponseCode.SECTION_TITLE_EXISTS, null);
        }
    }

    private void validateSectionTitleExistsOnUpdate(SectionAdminRequest request) {
        boolean isDuplicate = repository.existsByIdNotAndTitleEnIgnoreCaseOrTitleArIgnoreCase(
                request.getId(),
                request.getTitleEn(),
                request.getTitleAr(),
                request.getPage(),
                request.getContentType()
        );

        if (isDuplicate) {
            throw new CustomException(ApiResponseCode.SECTION_TITLE_EXISTS, null);
        }
    }

}

