package com.server.bitwit.module.ballot;

import com.server.bitwit.infra.exception.NotFoundException;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.ballot.dto.BallotRequest;
import com.server.bitwit.module.ballot.dto.UpdateBallotRequest;
import com.server.bitwit.module.common.BitwitResponse;
import com.server.bitwit.module.common.SimpleIdResponse;
import com.server.bitwit.module.domain.Ballot;
import com.server.bitwit.module.vote.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BallotService
{
    private final BallotRepository ballotRepository;
    
    private final AccountService accountService;
    private final VoteService    voteService;
    
    @Transactional
    public BitwitResponse createBallot(BallotRequest ballotRequest)
    {
        var account  = accountService.getAccount(ballotRequest.getAccountId( )).orElseThrow( );
        var vote     = voteService.getVote(ballotRequest.getVoteId( )).orElseThrow( );
        var ballot   = Ballot.createBallot(account, vote, ballotRequest.getVotingOption( ));
        var ballotId = ballotRepository.save(ballot).getId( );
        return SimpleIdResponse.of(ballotId);
    }
    
    @Transactional
    public void updateBallot(Long ballotId, UpdateBallotRequest request)
    {
        ballotRepository.findById(ballotId).orElseThrow(NotFoundException::new)
                        .update(request.getVotingOption( ));
    }
    
    @Transactional
    public void deleteBallot(Long ballotId)
    {
        var ballot = ballotRepository.findById(ballotId).orElseThrow(NotFoundException::new).delete( );
        ballotRepository.delete(ballot);
    }
    
    public boolean existBallotId(Long ballotId)
    {
        return ballotRepository.existsById(ballotId);
    }
}
