package com.rand.ishowApi.helpCenter.mobile.api.response;

import com.rand.ishowApi.metadata.HelpCenterMeta;
import lombok.Data;

import java.util.List;

@Data
public class HelpCenterMobileResponse {

    private List<HelpCenterMeta> helpCenterMetas;
}
