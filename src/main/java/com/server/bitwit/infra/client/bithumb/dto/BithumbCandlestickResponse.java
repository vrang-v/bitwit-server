package com.server.bitwit.infra.client.bithumb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Candlestick;
import lombok.Data;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BithumbCandlestickResponse {
    
    String status;
    
    @JsonProperty("data")
    Object[][] candlesticks;
    
    public List<Candlestick> getCandlesticks(String ticker) {
        return Arrays.stream(candlesticks)
                     .map(candlestick -> Candlestick
                             .builder( )
                             .ticker(ticker)
                             .dataTime(Instant.ofEpochMilli((long)candlestick[0]))
                             .openPrice(Double.parseDouble((String)candlestick[1]))
                             .closingPrice(Double.parseDouble((String)candlestick[2]))
                             .highPrice(Double.parseDouble((String)candlestick[3]))
                             .lowPrice(Double.parseDouble((String)candlestick[4]))
                             .tradingVolume(Double.parseDouble((String)candlestick[5]))
                             .build( )
                     )
                     .collect(Collectors.toList( ));
    }
}
