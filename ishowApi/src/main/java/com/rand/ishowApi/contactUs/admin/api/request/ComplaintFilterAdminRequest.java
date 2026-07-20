package com.rand.ishowApi.contactUs.admin.api.request;

import com.rand.ishowApi.common.PaginationFilter;
import lombok.Data;

@Data
public class ComplaintFilterAdminRequest extends PaginationFilter {

    private String status;
    private String phoneNumber;
    private String title;
}
