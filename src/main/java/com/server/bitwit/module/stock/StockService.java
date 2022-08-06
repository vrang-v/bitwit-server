package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.common.service.MappingService;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.stock.dto.CreateStockRequest;
import com.server.bitwit.module.stock.dto.StockResponse;
import com.server.bitwit.module.stock.dto.UpdateRealTimeInfoRequest;
import com.server.bitwit.module.stock.search.SearchStockCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StockService {
    
    private final StockRepository stockRepository;
    
    private final MappingService mappingService;
    
    public StockResponse createStock(CreateStockRequest request) {
        return Optional.ofNullable(mappingService.mapTo(request, Stock.class))
                       .map(stockRepository::save)
                       .map(stock -> mappingService.mapTo(stock, StockResponse.class))
                       .orElseThrow( );
    }
    
    public StockResponse getStockResponse(Long stockId) {
        return stockRepository.findById(stockId)
                              .map(stock -> mappingService.mapTo(stock, StockResponse.class))
                              .orElseThrow(( ) -> new NonExistentResourceException("stock", stockId));
    }
    
    public List<StockResponse> searchStocks(SearchStockCond cond, Pageable pageable) {
        return stockRepository
                .searchStockPage(cond, pageable)
                .stream( )
                .map(stock -> mappingService.mapTo(stock, StockResponse.class))
                .toList( );
    }
    
    public Page<StockResponse> searchStockPage(SearchStockCond cond, Pageable pageable) {
        return stockRepository.searchStockPage(cond, pageable)
                              .map(stock -> mappingService.mapTo(stock, StockResponse.class));
    }
    
    public Optional<Stock> findById(Long stockId) {
        return stockRepository.findById(stockId);
    }
    
    public boolean existById(Long stockId) {
        return stockRepository.existsById(stockId);
    }
    
    @Transactional
    public void updateRealTimeInfo(UpdateRealTimeInfoRequest request) {
        stockRepository.findByTicker(request.getTicker( ))
                       .orElseThrow(( ) -> new BitwitException(request.getTicker( ) + "로 찾을 수 없습니다"))
                       .updateCurrentPrice(request.getCurrentPrice( ))
                       .updateFluctuate24h(request.getFluctuate24h( ))
                       .updateFluctuateRate24h(request.getFluctuateRate24h( ));
    }
}
