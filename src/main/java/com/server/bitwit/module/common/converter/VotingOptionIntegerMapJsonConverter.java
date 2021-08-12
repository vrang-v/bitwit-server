package com.server.bitwit.module.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.domain.VotingOption;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;

@Converter
public class VotingOptionIntegerMapJsonConverter implements AttributeConverter<Map<VotingOption, Integer>, String> {
    
    private final ObjectMapper mapper = new ObjectMapper( );
    
    @Override
    public String convertToDatabaseColumn(Map<VotingOption, Integer> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        }
        catch (JsonProcessingException e) {
            throw new BitwitException(e.getMessage( ));
        }
    }
    
    @Override
    public Map<VotingOption, Integer> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>( ) { });
        }
        catch (JsonProcessingException e) {
            throw new BitwitException(e.getMessage( ));
        }
    }
}
