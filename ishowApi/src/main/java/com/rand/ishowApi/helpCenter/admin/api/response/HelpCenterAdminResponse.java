package com.rand.ishowApi.helpCenter.admin.api.response;

import com.rand.ishowApi.metadata.HelpCenterMeta;
import lombok.Data;

import java.util.List;

@Data
public class HelpCenterAdminResponse {

    private List<HelpCenterMeta> helpCenterMetas;
}
