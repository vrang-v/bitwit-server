package com.server.bitwit.module.stock;

import com.server.bitwit.module.stock.dto.StockRequest;
import com.server.bitwit.infra.exception.NotFoundException;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public StockResponse getStock(Long stockId)
    {
        return stockRepository.findById(stockId)
                              .map(StockResponse::fromStock)
                              .orElseThrow(( ) -> new NotFoundException("Requested stockId '" + stockId + "'"));
    }
}
