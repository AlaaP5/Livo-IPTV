package com.rand.ishowApi.contactUs.mobile.application.manager;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.contactUs.admin.domain.entity.Complaint;
import com.rand.ishowApi.contactUs.mobile.api.request.ComplaintMobileRequest;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class ComplaintDomainManager {

    public Complaint create(Account account, ComplaintMobileRequest complaintMobileRequest) {

        return Complaint.builder()
                .alternativePhone(complaintMobileRequest.getAlternativePhone())
                .title(complaintMobileRequest.getTitle())
                .description(complaintMobileRequest.getDescription())
                .email(complaintMobileRequest.getEmail())
                .isRead(false)
                .account(account)
                .build();
    }

    public void setIsRead(Complaint complaint, String status) {
        complaint.setIsRead(BooleanConverter.getActiveBoolean(status));
    }
}
