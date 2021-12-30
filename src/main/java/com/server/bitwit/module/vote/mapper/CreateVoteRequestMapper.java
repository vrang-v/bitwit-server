package com.server.bitwit.module.vote.mapper;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.vote.dto.CreateVoteRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(config = MapStructConfig.class, nullValuePropertyMappingStrategy = IGNORE)
public abstract class CreateVoteRequestMapper implements Converter<CreateVoteRequest, Vote> {
    
    @Lazy @Autowired StockService stockService;
    
    @ObjectFactory
    public Vote createVote(CreateVoteRequest request) {
        var stock = stockService.findById(request.getStockId( )).orElseThrow( );
        return Vote.createVote(stock, request.getDescription( ), request.getStartAt( ), request.getEndedAt( ));
    }
}
