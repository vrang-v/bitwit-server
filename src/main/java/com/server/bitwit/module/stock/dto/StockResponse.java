package com.server.bitwit.module.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Stock;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data @FieldDefaults(level = PRIVATE)
public class StockResponse {
    
    @JsonProperty("stockId")
    Long id;
    
    String ticker;
    
    String fullName;
    
    String koreanName;
    
    Double currentPrice;
    
    Double realTimeFluctuation;
    
    Double fluctuate24h;
    
    Double fluctuateRate24h;
}
