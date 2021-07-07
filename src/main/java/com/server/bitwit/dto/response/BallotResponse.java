package com.server.bitwit.dto.response;

import com.server.bitwit.domain.Ballot;
import com.server.bitwit.domain.VotingOption;
import lombok.Data;

@Data
public class BallotResponse
{
    Long id;
    
    AccountResponse account;
    
    Long voteId;
    
    VotingOption votingOption;
    
    public static BallotResponse fromBallot(Ballot ballot)
    {
        var response = new BallotResponse( );
        response.id           = ballot.getId( );
        response.voteId       = ballot.getVote( ).getId( );
        response.votingOption = ballot.getVotingOption( );
        response.account      = AccountResponse.fromAccount(ballot.getAccount( ));
        return response;
    }
}
