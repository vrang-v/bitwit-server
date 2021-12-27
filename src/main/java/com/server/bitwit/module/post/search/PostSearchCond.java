package com.server.bitwit.module.post.search;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostSearchCond {
    
    String keyword;
    
    List<Long> stockIds;
    
    List<String> tags;
    
    List<String> tickers;
    
    String writer;
    
    Long likerId;
    
    public void setStockId(List<Long> stockIds) {
        this.stockIds = stockIds;
    }
    
    public void setTicker(List<String> tickers) {
        this.tickers = tickers;
    }
}
