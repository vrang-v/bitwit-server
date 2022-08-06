package com.server.bitwit.restdocs;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

public class HideAuthorizationHeaderPreprocessor implements OperationPreprocessor {
    
    @Override
    public OperationRequest preprocess(OperationRequest request) {
        var headers = new HttpHeaders( );
        headers.putAll(request.getHeaders( ));
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer <hidden>");
        return new OperationRequestFactory( )
                .create(request.getUri( ), request.getMethod( ), request.getContent( ), headers,
                        request.getParameters( ), request.getParts( ), request.getCookies( ));
    }
    
    @Override
    public OperationResponse preprocess(OperationResponse response) {
        return null;
    }
    
}
