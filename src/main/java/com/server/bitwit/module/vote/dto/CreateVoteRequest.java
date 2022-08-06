package com.server.bitwit.module.vote.dto;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.stock.StockService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoteRequest {
    
    @NotNull
    Long stockId;
    
    @NotBlank
    String description;
    
    @NotNull
    LocalDateTime startAt;
    
    @NotNull
    LocalDateTime endedAt;
    
    @Mapper(config = MapStructConfig.class, nullValuePropertyMappingStrategy = IGNORE)
    public abstract static class CreateVoteRequestMapper implements Converter<CreateVoteRequest, Vote> {
        
        @Lazy @Autowired StockService stockService;
        
        @ObjectFactory
        public Vote createVote(CreateVoteRequest request) {
            var stock = stockService.findById(request.getStockId( )).orElseThrow( );
            return Vote.createVote(stock, request.getDescription( ), request.getStartAt( ), request.getEndedAt( ));
        }
    }
}
