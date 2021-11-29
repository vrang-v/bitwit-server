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
        var stringBuilder = new StringBuilder( );
        stringBuilder.append("요청된 id");
        if (id != null) {
            stringBuilder.append(" [")
                         .append(id)
                         .append("]");
        }
        stringBuilder.append("에 해당하는 ")
                     .append(StringUtils.hasText(property) ? "[" + property + "]" : "리소스")
                     .append("를 찾을 수 없습니다");
        return stringBuilder.toString( );
    }
}
