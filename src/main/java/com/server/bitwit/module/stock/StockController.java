package com.server.bitwit.module.stock;

import com.server.bitwit.module.candlestick.CandlestickRepository;
import com.server.bitwit.module.stock.dto.ChartResponse;
import com.server.bitwit.module.stock.dto.CreateStockRequest;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    
    private final StockService stockService;
    
    private final CandlestickRepository candlestickRepository;
    
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
    public List<StockResponse> searchStock(@ModelAttribute SearchStockCond cond, @PageableDefault Pageable pageable) {
        return stockService.searchStocks(cond, pageable);
    }
    
    @GetMapping("/{ticker}/bithumb/chart/24h")
    public List<ChartResponse> getCandlestick24hChart(@PathVariable String ticker, Pageable pageable) {
        return candlestickRepository
                .findAllByTicker(ticker, pageable)
                .stream( )
                .map(bithumbChart ->
                        new ChartResponse(
                                bithumbChart.getDataTime( ),
                                bithumbChart.getOpenPrice( ),
                                bithumbChart.getClosingPrice( ),
                                bithumbChart.getHighPrice( ),
                                bithumbChart.getLowPrice( ),
                                bithumbChart.getTradingVolume( )
                        )
                )
                .collect(Collectors.toList( ));
    }
}
