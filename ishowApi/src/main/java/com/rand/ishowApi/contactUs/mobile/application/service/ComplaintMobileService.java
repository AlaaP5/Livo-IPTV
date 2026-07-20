package com.rand.ishowApi.contactUs.mobile.application.service;

import com.rand.ishowApi.account.mobile.application.service.AccountMobileService;
import com.rand.ishowApi.contactUs.admin.domain.entity.Complaint;
import com.rand.ishowApi.contactUs.admin.domain.repository.ComplaintRepository;
import com.rand.ishowApi.contactUs.mobile.api.request.ComplaintMobileRequest;
import com.rand.ishowApi.contactUs.mobile.application.manager.ComplaintDomainManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ComplaintMobileService {

    private final ComplaintDomainManager complaintDomainManager;
    private final ComplaintRepository complaintRepository;
    private final AccountMobileService accountService;

    public void createComplaint(ComplaintMobileRequest complaintMobileRequest) {

        Complaint complaint = complaintDomainManager.create(accountService.getCurrentAccount(), complaintMobileRequest);

        complaintRepository.save(complaint);
    }
}
