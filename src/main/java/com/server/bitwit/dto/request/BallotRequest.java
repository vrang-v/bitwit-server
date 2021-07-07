package com.server.bitwit.dto.request;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Ballot;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.domain.VotingOption;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BallotRequest
{
    @NotNull
    Long accountId;
    
    @NotNull
    Long voteId;
    
    @NotNull
    VotingOption votingOption;
    
    public Ballot toBallot( )
    {
        var account = Account.onlyId(accountId);
        var vote    = Vote.onlyId(voteId);
        return Ballot.createBallot(account, vote, votingOption);
    }
}
