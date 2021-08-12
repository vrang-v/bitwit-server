package com.server.bitwit.module.vote;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.bitwit.domain.QVote;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.vote.search.VoteParticipationSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static com.server.bitwit.domain.QAccount.account;
import static com.server.bitwit.domain.QBallot.ballot;
import static com.server.bitwit.domain.QVote.vote;
import static com.server.bitwit.util.DynamicQueryUtils.eq;

@Slf4j
@RequiredArgsConstructor
@Repository
public class VoteQueryRepositoryImpl implements VoteQueryRepository {
    
    private final JPAQueryFactory query;
    
    @Override
    public Optional<Vote> findWithBallotWithAccountById(Long voteId) {
        return Optional.ofNullable(
                query.selectFrom(vote)
                     .leftJoin(vote.ballots, ballot).fetchJoin( )
                     .leftJoin(ballot.account, account).fetchJoin( )
                     .where(vote.id.eq(voteId))
                     .fetchOne( )
        );
    }
    
    @Override
    public List<Vote> findActiveVotes( ) {
        var now = LocalDateTime.now( );
        return query.selectFrom(vote)
                    .leftJoin(vote.stock).fetchJoin( )
                    .where(vote.startAt.before(now),
                            vote.endedAt.after(now))
                    .fetch( );
    }
    
    @Override
    public List<Vote> findAllByParticipation(VoteParticipationSearch search) {
        var query = this.query
                .selectFrom(vote)
                .innerJoin(vote.stock).fetchJoin( )
                .where(eq(vote.id, search.getVoteId( )));
        if (search.isParticipation( )) {
            query.innerJoin(vote.ballots, ballot).fetchJoin( )
                 .where(ballot.account.id.eq(search.getAccountId( )));
        }
        else {
            var _vote = new QVote("sub");
            var participatedVotes = selectFrom(_vote)
                    .innerJoin(_vote.ballots, ballot)
                    .where(ballot.account.id.eq(search.getAccountId( )));
            query.where(vote.notIn(participatedVotes));
        }
        return query.fetch( );
    }
    
    @Override
    public List<Vote> findAllByAccountIdAndParticipationDate(Long accountId, LocalDate date) {
        var from = date.atStartOfDay( ).toInstant(ZoneOffset.UTC);
        var to   = from.plus(1, ChronoUnit.DAYS);
        return query.selectFrom(vote)
                    .leftJoin(vote.ballots, ballot).fetchJoin( )
                    .leftJoin(ballot.account, account).fetchJoin( )
                    .leftJoin(vote.stock).fetchJoin( )
                    .where(account.id.eq(accountId),
                            ballot.createdAt.between(from, to))
                    .fetch( );
    }
}
