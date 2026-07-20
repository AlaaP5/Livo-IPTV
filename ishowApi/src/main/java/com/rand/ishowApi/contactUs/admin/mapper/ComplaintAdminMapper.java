package com.rand.ishowApi.contactUs.admin.mapper;

import com.rand.ishowApi.contactUs.admin.api.response.ComplaintAdminResponse;
import com.rand.ishowApi.contactUs.admin.domain.entity.Complaint;
import org.springframework.stereotype.Component;

@Component
public class ComplaintAdminMapper {

    public ComplaintAdminResponse toResponse(Complaint complaint) {

        ComplaintAdminResponse complaintAdminResponse = new ComplaintAdminResponse();

        complaintAdminResponse.setId(complaint.getId());
        complaintAdminResponse.setAlternativePhone(complaint.getAlternativePhone());
        complaintAdminResponse.setEmail(complaint.getEmail());
        complaintAdminResponse.setTitle(complaint.getTitle());
        complaintAdminResponse.setDescription(complaint.getDescription());
        complaintAdminResponse.setAccount(complaint.getAccount());

        complaintAdminResponse.setIsRead(complaint.getIsRead());

        return complaintAdminResponse;
    }
}
