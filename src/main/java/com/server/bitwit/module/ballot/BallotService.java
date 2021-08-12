package com.server.bitwit.module.ballot;

import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.ballot.dto.CreateOrChangeBallotRequest;
import com.server.bitwit.module.common.dto.BitwitResponse;
import com.server.bitwit.module.common.dto.SimpleIdResponse;
import com.server.bitwit.domain.Ballot;
import com.server.bitwit.module.vote.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

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
    public BitwitResponse createOrChangeBallot(Long accountId, CreateOrChangeBallotRequest request) {
        AtomicLong ballotId = new AtomicLong(- 1);
        ballotRepository.findByVoteAndAccountId(accountId, request.getVoteId( ))
                        .ifPresentOrElse(
                                ballot -> {
                                    var votingOption = ballot.getVotingOption( );
                                    if (votingOption == request.getVotingOption( )) {
                                        ballot.delete( );
                                        ballotRepository.delete(ballot);
                                    }
                                    else {
                                        ballot.update(request.getVotingOption( ));
                                    }
                                    ballotId.set(ballot.getId( ));
                                },
                                ( ) -> ballotId.set(createBallot(accountId, request))
                        );
        return SimpleIdResponse.of(ballotId.get( ));
    }
    
    private Long createBallot(Long accountId, CreateOrChangeBallotRequest request) {
        var account = accountService.getAccount(accountId).orElseThrow( );
        var vote    = voteService.findById(request.getVoteId( )).orElseThrow( );
        var ballot  = Ballot.createBallot(account, vote, request.getVotingOption( ));
        return ballotRepository.save(ballot).getId( );
    }
    
    public boolean existsById(Long ballotId) {
        return ballotRepository.existsById(ballotId);
    }
}
