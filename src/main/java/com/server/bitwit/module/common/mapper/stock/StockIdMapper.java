package com.server.bitwit.module.common.mapper.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.stock.StockRepository;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public abstract class StockIdMapper implements Converter<Long, Stock> {
    
    @Lazy @Autowired
    private StockRepository stockRepository;
    
    @ObjectFactory
    public Stock getStock(Long stockId) {
        if (stockId == null) { return null; }
        return stockRepository.findById(stockId)
                              .orElseThrow(( ) -> new NonExistentResourceException("stock", stockId));
    }
}
