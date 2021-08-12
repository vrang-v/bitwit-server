package com.server.bitwit.module.common.dto;

import lombok.Data;

@Data
public class SimpleIdResponse implements BitwitResponse {
    
    private Long id;
    
    public static SimpleIdResponse of(Long id) {
        var response = new SimpleIdResponse( );
        response.id = id;
        return response;
    }
}
