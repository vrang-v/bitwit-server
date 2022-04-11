package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.stock.dto.CreateStockRequest;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import com.server.bitwit.module.stock.dto.StockResponse;
import com.server.bitwit.module.stock.dto.UpdateRealTimeInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StockService {
    
    private final StockRepository stockRepository;
    
    private final ConversionService conversionService;
    
    public StockResponse createStock(CreateStockRequest request) {
        return Optional.ofNullable(conversionService.convert(request, Stock.class))
                       .map(stockRepository::save)
                       .map(stock -> conversionService.convert(stock, StockResponse.class))
                       .orElseThrow( );
    }
    
    public StockResponse getStockResponse(Long stockId) {
        return stockRepository.findById(stockId)
                              .map(stock -> conversionService.convert(stock, StockResponse.class))
                              .orElseThrow(( ) -> new NonExistentResourceException("stock", stockId));
    }
    
    public List<StockResponse> searchStocks(SearchStockCond cond, Pageable pageable) {
        return stockRepository
                .searchStockPage(cond, pageable)
                .stream( )
                .map(stock -> conversionService.convert(stock, StockResponse.class))
                .collect(Collectors.toList( ));
    }
    
    public Page<StockResponse> searchStockPage(SearchStockCond cond, Pageable pageable) {
        return stockRepository.searchStockPage(cond, pageable)
                              .map(stock -> conversionService.convert(stock, StockResponse.class));
    }
    
    public Optional<Stock> findById(Long stockId) {
        return stockRepository.findById(stockId);
    }
    
    public List<Stock> findAll( ) {
        return stockRepository.findAll( );
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
