package com.server.bitwit.module.vote.dto;

import com.server.bitwit.module.domain.Stock;
import com.server.bitwit.module.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    public Vote toVote(Stock stock)
    {
        return Vote.createVote(stock, description, startAt, endedAt);
    }
}
