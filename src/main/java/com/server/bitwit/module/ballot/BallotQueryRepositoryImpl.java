package com.server.bitwit.module.ballot;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.bitwit.domain.Ballot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.server.bitwit.domain.QAccount.account;
import static com.server.bitwit.domain.QBallot.ballot;
import static com.server.bitwit.domain.QVote.vote;

@RequiredArgsConstructor
@Repository
public class BallotQueryRepositoryImpl implements BallotQueryRepository {
    
    private final JPAQueryFactory query;
    
    @Override
    public Optional<Ballot> findByVoteIdAndAccountId(Long voteId, Long accountId) {
        return Optional.ofNullable(
                query.selectFrom(ballot)
                     .innerJoin(ballot.vote, vote)
                     .innerJoin(ballot.account, account)
                     .where(
                             vote.id.eq(voteId),
                             account.id.eq(accountId)
                     )
                     .fetchOne( )
        );
    }
}
