package com.server.bitwit.module.vote.dto;

import com.server.bitwit.module.domain.Stock;
import com.server.bitwit.module.domain.Vote;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class VoteRequest
{
    @NotNull
    Long stockId;
    
    @NotBlank
    String description;
    
    @NotNull
    LocalDateTime startAt;
    
    @NotNull
    LocalDateTime endedAt;
    
    public Vote toVote( )
    {
        var stock = Stock.onlyId(stockId);
        return Vote.createVote(stock, description, startAt, endedAt);
    }
}
