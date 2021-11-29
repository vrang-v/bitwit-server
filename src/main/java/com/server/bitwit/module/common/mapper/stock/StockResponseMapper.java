package com.server.bitwit.module.common.mapper.stock;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.stock.dto.StockResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public interface StockResponseMapper extends Converter<Stock, StockResponse> { }
