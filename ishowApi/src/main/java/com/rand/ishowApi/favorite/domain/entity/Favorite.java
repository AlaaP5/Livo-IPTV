package com.rand.ishowApi.favorite.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.shared.settings.ContentType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'accountId': 1, 'contentId': 1, 'contentType': 1}", unique = true)
public class Favorite extends MongoBaseEntity {
    @Id
    private String id;

    @Indexed
    private Long accountId;

    @Indexed
    private String contentId;

    @Indexed
    private ContentType contentType;

    @Indexed
    @Builder.Default
    private Boolean active = true;

}

