package com.server.bitwit.infra.client.bithumb;

import com.server.bitwit.infra.client.bithumb.dto.BithumbCandlestickResponse;
import com.server.bitwit.infra.client.bithumb.dto.BithumbTickerInfoResponse;
import com.server.bitwit.module.candlestick.CandlestickRepository;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.stock.dto.UpdateRealTimeInfoRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@Service
public class BithumbService {
    
    private final BithumbProperties     bithumbProperties;
    private final StockService          stockService;
    private final CandlestickRepository candlestickRepository;
    private final WebClient             webClient;
    
    public BithumbService(BithumbProperties bithumbProperties, StockService stockService, CandlestickRepository candlestickRepository, Builder webClientBuilder) {
        this.bithumbProperties     = bithumbProperties;
        this.stockService          = stockService;
        this.candlestickRepository = candlestickRepository;
        this.webClient             = webClientBuilder.baseUrl(bithumbProperties.getBaseUrl( )).build( );
    }
    
    public void updateRealTimePrice( ) {
        var tickerMap = bithumbProperties.getTickerMap( );
        tickerMap.keySet( ).forEach(ticker -> {
            var uri = bithumbProperties.getTickerInfoUri( ) + "/" + tickerMap.get(ticker);
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
                     .subscribe(stockService::updateRealTimeInfo);
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
                Thread.currentThread( ).interrupt( );
                throw new BitwitException(e.getMessage( ));
            }
        });
    }
    
    public void get24hCandleStickChart( ) {
        var tickerMap = bithumbProperties.getTickerMap( );
        tickerMap.keySet( ).forEach(ticker -> {
            var uri = bithumbProperties.getCandlestickUri( ) + "/" + tickerMap.get(ticker) + "/24h";
            webClient.get( )
                     .uri(uri)
                     .retrieve( )
                     .bodyToMono(BithumbCandlestickResponse.class)
                     .flatMapIterable(response -> response.getCandlesticks(ticker))
                     .subscribe(candlestickRepository::save);
        });
    }
}
