package com.rand.ishowApi.ads.admin.api.request;


import com.rand.ishowApi.common.PaginationFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdAdminFilterRequest extends PaginationFilter {

    private LocalDate startDate;
    private LocalDate endDate;
    private String active;
}
