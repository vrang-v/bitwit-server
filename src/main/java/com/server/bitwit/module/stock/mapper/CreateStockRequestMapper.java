package com.server.bitwit.module.stock.mapper;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.stock.dto.CreateStockRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public interface CreateStockRequestMapper extends Converter<CreateStockRequest, Stock> {
    
    @ObjectFactory
    default Stock createStock(CreateStockRequest request) {
        return Stock.createStock(request.getTicker( ), request.getFullName( ), request.getKoreanName( ));
    }
}
