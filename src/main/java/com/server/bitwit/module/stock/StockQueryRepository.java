package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.stock.search.SearchStockCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockQueryRepository {
    
    List<Stock> searchStocks(SearchStockCond cond);
    
    Page<Stock> searchStockPage(SearchStockCond cond, Pageable pageable);
}
