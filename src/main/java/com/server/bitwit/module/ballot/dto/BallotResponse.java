package com.server.bitwit.module.ballot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.VotingOption;
import com.server.bitwit.module.account.dto.AccountResponse;
import lombok.Data;

@Data
public class BallotResponse {
    
    @JsonProperty("ballotId")
    Long id;
    
    Long voteId;
    
    AccountResponse account;
    
    VotingOption votingOption;
    
    String status;
    
    public BallotResponse withStatus(String status) {
        this.status = status;
        return this;
    }
}
