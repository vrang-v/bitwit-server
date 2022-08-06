package com.server.bitwit.module.stock.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchStockCond {
    
    private String keyword;
    
    private List<String> tickers;
    
    public void setTicker(List<String> tickers) {
        this.tickers = tickers;
    }
}
