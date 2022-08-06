package com.server.bitwit.module.stock;

import com.server.bitwit.module.stock.dto.CreateStockRequest;
import com.server.bitwit.module.stock.search.SearchStockCond;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    
    private final StockService stockService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public StockResponse createStock(@Valid @RequestBody CreateStockRequest request) {
        return stockService.createStock(request);
    }
    
    @GetMapping("/{stockId}")
    public StockResponse getStock(@PathVariable Long stockId) {
        return stockService.getStockResponse(stockId);
    }
    
    @GetMapping("/search")
    public List<StockResponse> searchStock(@ModelAttribute SearchStockCond cond, Pageable pageable) {
        return stockService.searchStocks(cond, pageable);
    }
    
    @GetMapping("/search/page")
    public Page<StockResponse> searchStockPage(@ModelAttribute SearchStockCond cond, Pageable pageable) {
        return stockService.searchStockPage(cond, pageable);
    }
}
