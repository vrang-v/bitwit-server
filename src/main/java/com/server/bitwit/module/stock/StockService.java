package com.server.bitwit.module.stock;

import com.server.bitwit.module.domain.Stock;
import com.server.bitwit.module.stock.dto.StockRequest;
import com.server.bitwit.infra.exception.NotFoundException;
import com.server.bitwit.module.stock.dto.StockDefaultResponse;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StockService
{
    private final StockRepository stockRepository;
    
    @Transactional
    public Long createStock(StockRequest request)
    {
        return stockRepository.save(request.toStock( )).getId( );
    }
    
    public StockResponse getStockResponse(Long stockId)
    {
        return getStock(stockId)
                              .map(StockDefaultResponse::fromStock)
                              .orElseThrow(( ) -> new NotFoundException("Requested stockId '" + stockId + "'"));
    }
    
    public Optional<Stock> getStock(Long stockId)
    {
        return stockRepository.findById(stockId);
    }
    
    public boolean existById(Long stockId)
    {
        return stockRepository.existsById(stockId);
    }
}
