package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.stock.dto.SearchStockCond;

import java.util.List;

public interface StockQueryRepository {
    
    List<Stock> searchStocks(SearchStockCond cond);
    
}
