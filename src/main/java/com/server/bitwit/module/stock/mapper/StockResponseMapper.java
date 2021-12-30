package com.server.bitwit.module.stock.mapper;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.stock.dto.StockResponse;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public interface StockResponseMapper extends Converter<Stock, StockResponse> { }
