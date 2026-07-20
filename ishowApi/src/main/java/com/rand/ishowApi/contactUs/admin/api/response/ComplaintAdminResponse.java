package com.rand.ishowApi.contactUs.admin.api.response;

import com.rand.ishowApi.account.domain.entity.Account;
import lombok.Data;

@Data
public class ComplaintAdminResponse {

    private Long id;
    private String alternativePhone;
    private String email;
    private String title;
    private String description;
    private Account account;
    private Boolean isRead;
}
