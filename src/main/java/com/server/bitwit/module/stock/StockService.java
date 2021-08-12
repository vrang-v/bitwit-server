package com.server.bitwit.module.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.NotFoundException;
import com.server.bitwit.module.stock.dto.StockDefaultResponse;
import com.server.bitwit.module.stock.dto.StockRequest;
import com.server.bitwit.module.stock.dto.StockResponse;
import com.server.bitwit.module.stock.dto.UpdateRealTimeInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StockService {
    
    private final StockRepository stockRepository;
    
    @Transactional
    public Long createStock(StockRequest request) {
        return stockRepository.save(request.toStock( )).getId( );
    }
    
    public StockResponse getStockResponse(Long stockId) {
        return stockRepository
                .findById(stockId)
                .map(StockDefaultResponse::fromStock)
                .orElseThrow(( ) -> new NotFoundException("Requested stockId '" + stockId + "'"));
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
