package com.xjudge.service.email.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter
public class JsonDataConverter implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> metaData) {
        try {
            return objectMapper.writeValueAsString(metaData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization error", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Deserialization error", e);
        }
    }
}
