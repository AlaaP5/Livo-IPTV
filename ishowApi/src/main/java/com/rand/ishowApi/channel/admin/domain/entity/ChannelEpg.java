package com.rand.ishowApi.channel.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelEpg extends MongoBaseEntity {
    @Id
    private String id;

    @Indexed
    private String channelId;
    @Indexed
    private LocalDateTime startDate;
    @Indexed
    private LocalDateTime endDate;

    @TextIndexed
    private String titleEn;
    @TextIndexed
    private String titleAr;

    @Indexed
    private Boolean active;
}
