package com.server.bitwit.module.ballot;

import com.server.bitwit.domain.Ballot;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.module.ballot.dto.CreateOrChangeBallotRequest;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.vote.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BallotService {
    
    private final BallotRepository ballotRepository;
    
    private final AccountService    accountService;
    private final VoteService       voteService;
    private final ConversionService conversionService;
    
    @Transactional
    public BallotResponse createOrChangeBallot(Long accountId, CreateOrChangeBallotRequest request) {
        var response = new AtomicReference<BallotResponse>( );
        ballotRepository
                .findByVoteIdAndAccountId(request.getVoteId( ), accountId)
                .ifPresentOrElse(
                        ballot -> response.set(
                                ballot.getVotingOption( ) == request.getVotingOption( )
                                ? deleteBallot(ballot)
                                : updateBallot(request, ballot)
                        ),
                        ( ) -> response.set(createBallot(accountId, request).withStatus("CREATED"))
                );
        return response.get( );
    }
    
    private BallotResponse updateBallot(CreateOrChangeBallotRequest request, Ballot ballot) {
        ballot.update(request.getVotingOption( ));
        var response = conversionService.convert(ballot, BallotResponse.class);
        return response == null ? null : response.withStatus("UPDATED");
    }
    
    private BallotResponse deleteBallot(Ballot ballot) {
        ballotRepository.safeDelete(ballot);
        var response = conversionService.convert(ballot, BallotResponse.class);
        return response == null ? null : response.withStatus("DELETED");
    }
    
    private BallotResponse createBallot(Long accountId, CreateOrChangeBallotRequest request) {
        var account = accountService.findById(accountId)
                                    .orElseThrow(( ) -> new NonExistentResourceException("account", accountId));
        var vote = voteService.findById(request.getVoteId( ))
                              .orElseThrow(( ) -> new NonExistentResourceException("vote", request.getVoteId( )));
        var ballot      = Ballot.createBallot(account, vote, request.getVotingOption( ));
        var savedBallot = ballotRepository.save(ballot);
        return conversionService.convert(savedBallot, BallotResponse.class);
    }
    
    public boolean existsById(Long ballotId) {
        return ballotRepository.existsById(ballotId);
    }
}
