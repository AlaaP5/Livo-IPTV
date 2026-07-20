package com.rand.ishowApi.contactUs.admin.application.service;

import com.rand.ishowApi.contactUs.admin.api.request.ComplaintFilterAdminRequest;
import com.rand.ishowApi.contactUs.admin.api.response.ComplaintAdminResponse;
import com.rand.ishowApi.contactUs.admin.domain.entity.Complaint;
import com.rand.ishowApi.contactUs.admin.domain.repository.ComplaintRepository;
import com.rand.ishowApi.contactUs.admin.domain.repository.ComplaintSpecification;
import com.rand.ishowApi.contactUs.admin.mapper.ComplaintAdminMapper;
import com.rand.ishowApi.contactUs.mobile.application.manager.ComplaintDomainManager;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.data.jpa.domain.Specification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplaintAdminService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintAdminMapper complaintAdminMapper;
    private final ComplaintDomainManager complaintDomainManager;

    public Page<ComplaintAdminResponse> filterComplaint(ComplaintFilterAdminRequest complaintFilterAdminRequest) {

        Pageable pageable = PageRequest.of(
                complaintFilterAdminRequest.getPage() -1,
                complaintFilterAdminRequest.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Specification<Complaint> specification = Specification
                .where(ComplaintSpecification.isRead(BooleanConverter.getActiveBoolean(complaintFilterAdminRequest.getStatus())))
                .and(ComplaintSpecification.alternativePhoneEquals(complaintFilterAdminRequest.getPhoneNumber()))
                .and(ComplaintSpecification.titleContains(complaintFilterAdminRequest.getTitle()));

        return complaintRepository.findAll(specification, pageable)
                .map(complaintAdminMapper::toResponse);
    }

    public ComplaintAdminResponse changeComplaintStatus(Long id, String status) {

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.TAG_NOT_EXISTS, null));

        complaintDomainManager.setIsRead(complaint, status);

        complaintRepository.save(complaint);

        return complaintAdminMapper.toResponse(complaint);
    }
}
