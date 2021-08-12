package com.server.bitwit.domain;

import com.server.bitwit.module.error.exception.BitwitException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Ballot extends BaseTimeEntity {
    
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "voter_id")
    Long id;
    
    @ManyToOne(fetch = LAZY)
    Account account;
    
    @ManyToOne(fetch = LAZY)
    Vote vote;
    
    @Enumerated(STRING)
    VotingOption votingOption;
    
    public static Ballot createBallot(Account account, Vote vote, VotingOption votingOption) {
        if (isAlreadyVotedAccount(vote, account)) {
            throw new BitwitException("이미 투표에 참여한 계정입니다.");
        }
        var ballot = new Ballot( );
        ballot.account      = account;
        ballot.vote         = vote;
        ballot.votingOption = votingOption;
        vote.addBallot(votingOption);
        return ballot;
    }
    
    private static boolean isAlreadyVotedAccount(Vote vote, Account account) {
        return vote.getBallots( )
                   .stream( )
                   .map(Ballot::getAccount)
                   .map(Account::getId)
                   .anyMatch(account.getId( )::equals);
    }
    
    public Ballot update(VotingOption votingOption) {
        var previousSelection = this.votingOption;
        if (previousSelection == votingOption) {
            return this;
        }
        this.votingOption = votingOption;
        vote.changeBallot(previousSelection, votingOption);
        return this;
    }
    
    public Ballot delete( ) {
        vote.removeBallot(this.votingOption);
        return this;
    }
}
