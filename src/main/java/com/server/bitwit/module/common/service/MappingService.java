package com.server.bitwit.module.common.service;

import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MappingService {
    
    private final ConversionService conversionService;
    
    public <T> T mapTo(Object source, Class<T> targetType) {
        var result = conversionService.convert(source, targetType);
        if (result == null) {
            throw new BitwitException(ErrorCode.SERVER_ERROR);
        }
        return result;
    }
}
