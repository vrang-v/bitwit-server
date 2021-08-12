package com.server.bitwit.module.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRealTimeInfoRequest {
    
    String ticker;
    
    Double currentPrice;
    
    Double fluctuate24h;
    
    Double fluctuateRate24h;
    
    public UpdateRealTimeInfoRequest(String ticker, String currentPrice, String fluctuate24h, String fluctuateRate24h) {
        this.ticker           = ticker;
        this.currentPrice     = Double.parseDouble(currentPrice);
        this.fluctuate24h     = Double.parseDouble(fluctuate24h);
        this.fluctuateRate24h = Double.parseDouble(fluctuateRate24h);
    }
}
