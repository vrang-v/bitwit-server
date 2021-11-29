package com.server.bitwit.module.vote.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteSearchCond {
    
    List<String> tickers;
    
    List<Long> voteIds;
    
    List<Long> stockIds;
    
    @NotNull
    Long accountId;
    
    public VoteSearchCond withAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }
    
    public void setTicker(List<String> tickers) {
        this.tickers = tickers;
    }
    
    public void setVoteId(List<Long> voteIds) {
        this.voteIds = voteIds;
    }
    
    public void setStockId(List<Long> stockIds) {
        this.stockIds = stockIds;
    }
}
