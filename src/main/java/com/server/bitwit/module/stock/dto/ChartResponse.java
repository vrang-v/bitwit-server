package com.server.bitwit.module.stock.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChartResponse {
    
    Instant dataTime;
    
    Double openPrice;
    
    Double closingPrice;
    
    Double highPrice;
    
    Double lowPrice;
    
    Double tradingVolume;
    
}
