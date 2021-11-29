package com.server.bitwit.module.post.search;

import lombok.Data;

import java.util.List;

@Data
public class PostSearchCond {
    
    String keyword;
    
    List<Long> stockIds;
    
    List<String> tickers;
    
    String writer;
    
    public void setStockId(List<Long> stockIds) {
        this.stockIds = stockIds;
    }
    
    public void setTicker(List<String> tickers) {
        this.tickers = tickers;
    }
}
