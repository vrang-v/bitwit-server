package com.server.bitwit.infra.client.bithumb;

import com.server.bitwit.infra.client.bithumb.dto.BithumbTickerInfoResponse;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.stock.dto.UpdateRealTimeInfoRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BithumbService {
    
    private final StockService stockService;
    
    private final BithumbProperties bithumbProperties;
    
    private final WebClient webClient;
    
    public BithumbService(StockService stockService, BithumbProperties bithumbProperties) {
        this.stockService      = stockService;
        this.bithumbProperties = bithumbProperties;
        this.webClient         = WebClient.create(bithumbProperties.getBaseUrl( ));
    }
    
    public void updateAllRealTimePrice( ) {
        var tickerMap = bithumbProperties.getTickerMap( );
        tickerMap.keySet( )
                 .forEach(ticker -> {
                     updateRealTimePrice(ticker);
                     try {
                         Thread.sleep(10L);
                     }
                     catch (InterruptedException e) {
                         Thread.currentThread( ).interrupt( );
                         throw new BitwitException(e.getMessage( ));
                     }
                 });
    }
    
    public void updateRealTimePrice(String ticker) {
        var tickerMap = bithumbProperties.getTickerMap( );
        var uri       = bithumbProperties.getTickerInfoUri( ) + "/" + tickerMap.get(ticker);
        webClient.get( )
                 .uri(uri)
                 .retrieve( )
                 .bodyToMono(BithumbTickerInfoResponse.class)
                 .map(response -> {
                     var currentPrice  = response.getData( ).getClosingPrice( );
                     var fluctuate     = response.getData( ).getFluctuate24h( );
                     var fluctuateRate = response.getData( ).getFluctuateRate24h( );
                     return new UpdateRealTimeInfoRequest(ticker, currentPrice, fluctuate, fluctuateRate);
                 })
                 .subscribe(stockService::updateRealTimeInfo, e -> { throw new BitwitException(e.getMessage( )); });
    }
}
