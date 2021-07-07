package com.server.bitwit.module.ballot;

import com.server.bitwit.module.domain.Account;
import com.server.bitwit.module.domain.Ballot;
import com.server.bitwit.module.domain.Vote;
import com.server.bitwit.module.domain.VotingOption;
import com.server.bitwit.module.ballot.dto.BallotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BallotService
{
    private final BallotRepository ballotRepository;
    
    @Transactional
    public Long createBallot(BallotRequest ballotRequest)
    {
        return ballotRepository.save(ballotRequest.toBallot( )).getId( );
    }
    
    @Transactional
    public Long createBallot(Long accountId, Long voteId, VotingOption votingOption)
    {
        var account = Account.onlyId(accountId);
        var vote    = Vote.onlyId(voteId);
        var ballot  = Ballot.createBallot(account, vote, votingOption);
        return ballotRepository.save(ballot).getId( );
    }
    
    public boolean existBallotId(Long ballotId)
    {
        return ballotRepository.existsById(ballotId);
    }
}
