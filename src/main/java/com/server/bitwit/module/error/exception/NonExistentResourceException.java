package com.server.bitwit.module.error.exception;

import org.springframework.util.StringUtils;

import static com.server.bitwit.module.error.exception.ErrorCode.RESOURCE_NOT_FOUND;

public class NonExistentResourceException extends BitwitException {
    
    private static final ErrorCode ERROR_CODE = RESOURCE_NOT_FOUND;
    
    public NonExistentResourceException(String property, Long id) {
        super(ERROR_CODE, getErrorMessage(property, id));
    }
    
    public NonExistentResourceException(Long id) {
        super(ERROR_CODE, getErrorMessage(null, id));
    }
    
    public NonExistentResourceException(String property) {
        super(ERROR_CODE, getErrorMessage(property, null));
    }
    
    private static String getErrorMessage(String property, Long id) {
        return String.format("요청된 %s 해당되는 %s 존재하지 않습니다.", makeIdString(id), makePropertyString(property));
    }
    
    private static String makeIdString(Long id) {
        return id != null ? String.format("id [%d]에", id) : "조건에";
    }
    
    private static String makePropertyString(String property) {
        return StringUtils.hasText(property) ? "[" + property + "]가" : "리소스가";
    }
}
