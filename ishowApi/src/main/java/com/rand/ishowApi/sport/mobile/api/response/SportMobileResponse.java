package com.rand.ishowApi.sport.mobile.api.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonPropertyOrder({
        "upcomingMatches",
        "sections"
})
@Data
public class SportMobileResponse {
    private List<UpcomingMatchResponse> upcomingMatches;
    private List<SportSectionResponse> sections;

}
