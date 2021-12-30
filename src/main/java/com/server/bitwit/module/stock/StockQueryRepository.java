package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockQueryRepository {
    
    List<Stock> searchStocks(SearchStockCond cond);
    
    Page<Stock> searchStocks(SearchStockCond cond, Pageable pageable);
}