package com.rand.ishowApi.sport.admin.api.response;
import lombok.Data;
@Data
public class ChampionAdminResponse {
    private Long id;
    private String nameEn;
    private String nameAr;
    private String logoPath;
    private Boolean active;
}
