package com.rand.ishowApi.sport.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpcomingMatch extends MongoBaseEntity {
    @Id
    private String id;

    private TeamMeta homeTeam;
    private TeamMeta awayTeam;
    private ChampionMeta champion;
    private ChannelMeta channel;

    private LocalDateTime matchDate;
    private Boolean active;


}
