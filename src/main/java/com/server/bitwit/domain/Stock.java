package com.server.bitwit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Stock extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "stock_id")
    Long id;
    
    String ticker;
    
    String fullName;
    
    String koreanName;
    
    Double currentPrice;
    
    Double realTimeFluctuation;
    
    Double fluctuate24h;
    
    Double fluctuateRate24h;
    
    Double marketCap;
    
    @ElementCollection
    @CollectionTable
    List<DailyInfo> dailyInfo;
    
    public static Stock createStock(String ticker, String fullName, String koreanName) {
        var stock = new Stock( );
        stock.ticker     = ticker;
        stock.fullName   = fullName;
        stock.koreanName = koreanName;
        return stock;
    }
    
    public Stock updateCurrentPrice(Double currentPrice) {
        this.realTimeFluctuation = this.currentPrice == null ? currentPrice : currentPrice - this.currentPrice;
        this.currentPrice        = currentPrice;
        return this;
    }
    
    public Stock updateFluctuate24h(Double fluctuate24h) {
        this.fluctuate24h = fluctuate24h;
        return this;
    }
    
    public Stock updateFluctuateRate24h(Double fluctuateRate24h) {
        this.fluctuateRate24h = fluctuateRate24h;
        return this;
    }
}
