package com.rand.ishowApi.helpCenter.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.HelpCenterMeta;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelpCenter extends MongoBaseEntity {

    @Id
    private String id;

    private List<HelpCenterMeta> helpCenterMetas;
}
