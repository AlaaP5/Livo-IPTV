package com.rand.ishowApi.openSearch.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.json.JsonData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonDataConverter {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);

    public <D> D convert(JsonData json, Class<D> clazz) {
        try {
            return objectMapper.readValue(json.toJson().toString(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public  <D> D convertSource(JsonNode source, Class<D> documentClass) {
        if (source == null || source.isNull()) {
            return null;
        }

        try {
            return objectMapper.treeToValue(source, documentClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
