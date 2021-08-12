package com.server.bitwit.infra.client.bithumb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties("bithumb")
public class BithumbProperties {
    
    private String baseUrl;
    
    private String tickerInfoUri;
    
    private String candlestickUri;
    
    private Map<String, String> tickerMap;
    
}
