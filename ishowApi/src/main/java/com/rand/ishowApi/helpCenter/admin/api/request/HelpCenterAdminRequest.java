package com.rand.ishowApi.helpCenter.admin.api.request;

import com.rand.ishowApi.metadata.HelpCenterMeta;
import lombok.Data;

import java.util.List;

@Data
public class HelpCenterAdminRequest {

    private List<HelpCenterMeta> helpCenterMetas;
}
