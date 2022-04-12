package com.server.bitwit.module.vote;

import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.vote.search.VoteSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoteQueryRepository {
    
    Optional<Vote> findWithBallotWithAccountById(Long voteId);
    
    List<Vote> findAllByAccountIdAndParticipationDate(Long accountId, LocalDate date);
    
    List<Vote> findActiveVotes( );
    
    List<Vote> searchAllParticipated(VoteSearchCond cond);
    
    List<Vote> searchAllNotParticipated(VoteSearchCond cond);
    
    Page<Vote> searchActiveVotePage(VoteSearchCond cond, Pageable pageable, LocalDateTime currentTime);
    
    List<Vote> searchAllActiveVotesParticipated(VoteSearchCond cond, LocalDateTime currentTime);
    
    List<Vote> searchActiveVoteWithOffset(VoteSearchCond cond, Sort sort, int offset, int limit, LocalDateTime currentTime);
    
}
