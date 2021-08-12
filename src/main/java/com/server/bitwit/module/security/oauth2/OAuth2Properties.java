package com.server.bitwit.module.security.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("oauth2")
public class OAuth2Properties {
    
    private List<String> authorizedRedirectUris;
    
    private String defaultCompleteUrl;
}
