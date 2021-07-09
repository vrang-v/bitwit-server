package com.server.bitwit.module.ballot.validation;

import com.server.bitwit.infra.exception.BitwitException;
import com.server.bitwit.infra.exception.NotFoundException;
import com.server.bitwit.module.account.AccountService;
import com.server.bitwit.module.ballot.dto.UpdateBallotRequest;
import com.server.bitwit.module.domain.Account;
import com.server.bitwit.module.domain.Ballot;
import com.server.bitwit.module.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class UpdateBallotRequestValidator implements Validator
{
    private final AccountService accountService;
    private final VoteService    voteService;
    
    @Override
    public boolean supports(Class<?> clazz)
    {
        return UpdateBallotRequest.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors)
    {
        var request   = (UpdateBallotRequest)target;
        var accountId = request.getAccountId( );
        var voteId    = request.getVoteId( );
        
        if (! accountService.existsById(accountId)) {
            throw new NotFoundException("해당하는 데이터를 찾을 수 없습니다. accountId: " + accountId);
        }
        
        if (! voteService.existsById(voteId)) {
            throw new NotFoundException("해당하는 데이터를 찾을 수 없습니다. voteId: " + voteId);
        }
        
        if (! isVotedAccount(accountId, voteId)) {
            throw new BitwitException("투표에 참여하지 않은 계정입니다. 투표를 먼저 진행해주세요.");
        }
    }
    
    private boolean isVotedAccount(Long accountId, Long voteId)
    {
        return voteService.getVote(voteId).orElseThrow( )
                          .getBallots( )
                          .stream( )
                          .map(Ballot::getAccount)
                          .map(Account::getId)
                          .anyMatch(accountId::equals);
    }
}
