package com.server.bitwit.module.stock;

import com.server.bitwit.module.candlestick.CandlestickRepository;
import com.server.bitwit.module.stock.dto.ChartResponse;
import com.server.bitwit.module.stock.dto.StockRequest;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {
    
    private final StockService stockService;
    
    private final CandlestickRepository candlestickRepository;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public void createStock(@Valid @RequestBody StockRequest request) {
        stockService.createStock(request);
    }
    
    @GetMapping("/{stockId}")
    public StockResponse getStock(@PathVariable Long stockId) {
        return stockService.getStockResponse(stockId);
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
