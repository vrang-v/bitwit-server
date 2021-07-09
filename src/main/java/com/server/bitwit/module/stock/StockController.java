package com.server.bitwit.module.stock;

import com.server.bitwit.module.stock.dto.StockRequest;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stocks")
public class StockController
{
    private final StockService stockService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public void createStock(@Valid @RequestBody StockRequest request)
    {
        stockService.createStock(request);
    }
    
    @GetMapping("/{stockId}")
    public StockResponse getStock(@PathVariable Long stockId)
    {
        return stockService.getStockResponse(stockId);
    }
}
