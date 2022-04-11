package com.server.bitwit.module.vote;

import com.server.bitwit.domain.QVote;
import com.server.bitwit.domain.Vote;
import com.server.bitwit.module.vote.search.VoteSearchCond;
import com.server.bitwit.util.QuerydslRepositoryBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.server.bitwit.domain.QAccount.account;
import static com.server.bitwit.domain.QBallot.ballot;
import static com.server.bitwit.domain.QVote.vote;
import static com.server.bitwit.util.DynamicQueryUtils.combineWithOr;
import static com.server.bitwit.util.DynamicQueryUtils.in;
import static java.time.LocalDateTime.now;

@Repository
public class VoteQueryRepositoryImpl extends QuerydslRepositoryBase implements VoteQueryRepository {
    
    protected VoteQueryRepositoryImpl( ) {
        super(Vote.class);
    }
    
    @Override
    public Optional<Vote> findWithBallotWithAccountById(Long voteId) {
        return Optional.ofNullable(
                selectFrom(vote)
                        .leftJoin(ballot.account, account).fetchJoin( )
                        .leftJoin(vote.ballots, ballot)
                        .where(vote.id.eq(voteId))
                        .fetchOne( )
        );
    }
    
    @Override
    public List<Vote> findActiveVotes( ) {
        var now = now( );
        return selectFrom(vote)
                .leftJoin(vote.stock).fetchJoin( )
                .where(
                        vote.startAt.before(now),
                        vote.endedAt.after(now)
                )
                .fetch( );
    }
    
    @Override
    public List<Vote> searchAllParticipated(VoteSearchCond cond) {
        return selectFrom(vote)
                .innerJoin(vote.stock).fetchJoin( )
                .innerJoin(vote.ballots, ballot)
                .where(
                        ballot.account.id.eq(cond.getAccountId( )),
                        combineWithOr(
                                in(vote.id, cond.getVoteIds( )),
                                in(vote.stock.ticker, cond.getTickers( )),
                                in(vote.stock.id, cond.getStockIds( ))
                        )
                )
                .orderBy(vote.endedAt.desc( ))
                .fetch( );
    }
    
    @Override
    public List<Vote> searchAllNotParticipated(VoteSearchCond cond) {
        var _vote = new QVote("sub");
        var participatedVotes = selectFrom(_vote)
                .innerJoin(_vote.ballots, ballot)
                .where(ballot.account.id.eq(cond.getAccountId( )));
        return selectFrom(vote)
                .innerJoin(vote.stock).fetchJoin( )
                .where(
                        vote.notIn(participatedVotes),
                        combineWithOr(
                                in(vote.id, cond.getVoteIds( )),
                                in(vote.stock.ticker, cond.getTickers( )),
                                in(vote.stock.id, cond.getStockIds( ))
                        )
                )
                .orderBy(vote.endedAt.desc( ))
                .fetch( );
    }
    
    @Override
    public Page<Vote> searchActiveVotePage(VoteSearchCond cond, Pageable pageable, LocalDateTime currentTime) {
        return paginate(pageable,
                selectFrom(vote)
                        .innerJoin(vote.stock).fetchJoin( )
                        .innerJoin(vote.ballots, ballot)
                        .where(
                                combineWithOr(
                                        in(vote.id, cond.getVoteIds( )),
                                        in(vote.stock.ticker, cond.getTickers( )),
                                        in(vote.stock.id, cond.getStockIds( ))
                                ),
                                vote.startAt.before(currentTime),
                                vote.endedAt.after(currentTime)
                        )
        );
    }
    
    @Override
    public List<Vote> searchAllActiveVotesParticipated(VoteSearchCond cond, LocalDateTime currentTime) {
        return selectFrom(vote)
                .innerJoin(vote.stock).fetchJoin( )
                .innerJoin(vote.ballots, ballot)
                .where(
                        ballot.account.id.eq(cond.getAccountId( )),
                        combineWithOr(
                                in(vote.id, cond.getVoteIds( )),
                                in(vote.stock.ticker, cond.getTickers( )),
                                in(vote.stock.id, cond.getStockIds( ))
                        ),
                        vote.startAt.before(currentTime),
                        vote.endedAt.after(currentTime)
                )
                .fetch( );
    }
    
    @Override
    public List<Vote> findAllByAccountIdAndParticipationDate(Long accountId, LocalDate date) {
        var from = date.atStartOfDay( ).toInstant(ZoneOffset.UTC);
        var to   = from.plus(1, ChronoUnit.DAYS);
        return selectFrom(vote)
                .leftJoin(vote.stock).fetchJoin( )
                .leftJoin(vote.ballots, ballot)
                .leftJoin(ballot.account, account)
                .where(
                        account.id.eq(accountId),
                        ballot.createdAt.between(from, to)
                )
                .fetch( );
    }
}
