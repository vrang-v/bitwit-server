package com.server.bitwit.module.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.domain.Authority;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class AuthorityListJsonConverter implements AttributeConverter<List<Authority>, String> {
    
    private final ObjectMapper mapper = new ObjectMapper( );
    
    @Override
    public String convertToDatabaseColumn(List<Authority> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        }
        catch (JsonProcessingException e) {
            throw new BitwitException(e.getMessage( ));
        }
    }
    
    @Override
    public List<Authority> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>( ) { });
        }
        catch (JsonProcessingException e) {
            throw new BitwitException(e.getMessage( ));
        }
    }
}
