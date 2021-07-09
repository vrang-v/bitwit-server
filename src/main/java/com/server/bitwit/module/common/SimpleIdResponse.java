package com.server.bitwit.module.common;

import lombok.Data;

@Data
public class SimpleIdResponse implements BitwitResponse
{
    Long id;
    
    public static SimpleIdResponse of(Long id)
    {
        var response = new SimpleIdResponse( );
        response.id = id;
        return response;
    }
}
