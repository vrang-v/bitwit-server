package com.server.bitwit.service;

import com.server.bitwit.dto.request.StockRequest;
import com.server.bitwit.dto.response.StockResponse;
import com.server.bitwit.exception.NotFoundException;
import com.server.bitwit.repository.StockRepository;
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
