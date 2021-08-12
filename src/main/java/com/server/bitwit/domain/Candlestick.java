package com.server.bitwit.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@Builder @AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Candlestick {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candlestick_id")
    Long id;
    
    String ticker;
    
    Instant dataTime;
    
    Double openPrice;
    
    Double closingPrice;
    
    Double highPrice;
    
    Double lowPrice;
    
    Double tradingVolume;
}
