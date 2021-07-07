package com.server.bitwit.controller;

import com.server.bitwit.dto.request.StockRequest;
import com.server.bitwit.dto.response.StockResponse;
import com.server.bitwit.service.StockService;
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
        return stockService.getStock(stockId);
    }
}
