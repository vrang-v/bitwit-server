package com.server.bitwit.infra.client.bithumb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BithumbTickerInfoResponse {
    
    @JsonProperty("status")
    String status;
    
    @JsonProperty("data")
    Data data;
    
    @lombok.Data
    public static class Data {
        @JsonProperty("opening_price")
        String openingPrice;
        
        @JsonProperty("closing_price")
        String closingPrice;
        
        @JsonProperty("min_price")
        String minPrice;
        
        @JsonProperty("max_price")
        String maxPrice;
        
        @JsonProperty("units_traded")
        String unitsTraded;
        
        @JsonProperty("acc_trade_value")
        String accTradeValue;
        
        @JsonProperty("prev_closing_price")
        String prevClosingPrice;
        
        @JsonProperty("units_traded_24H")
        String unitsTraded24h;
        
        @JsonProperty("acc_trade_value_24H")
        String accTradeValue24h;
        
        @JsonProperty("fluctate_24H")
        String fluctuate24h;
        
        @JsonProperty("fluctate_rate_24H")
        String fluctuateRate24h;
        
        @JsonProperty("date")
        String date;
    }
}
