package com.server.bitwit.module.domain;

import com.server.bitwit.module.common.BaseTimeEntity;
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
public class Ballot extends BaseTimeEntity
{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "voter_id")
    Long id;
    
    @ManyToOne(fetch = LAZY)
    Account account;
    
    @ManyToOne(fetch = LAZY)
    Vote vote;
    
    @Enumerated(STRING)
    VotingOption votingOption;
    
    public static Ballot createBallot(Account account, Vote vote, VotingOption votingOption)
    {
        var ballot = new Ballot( );
        ballot.account      = account;
        ballot.vote         = vote;
        ballot.votingOption = votingOption;
        vote.addBallot(votingOption);
        return ballot;
    }
    
    public Ballot update(VotingOption votingOption)
    {
        var previousSelection = this.votingOption;
        if (previousSelection == votingOption) {
            return this;
        }
        this.votingOption = votingOption;
        vote.changeBallot(previousSelection, votingOption);
        return this;
    }
    
    public Ballot delete( )
    {
        vote.removeBallot(this.votingOption);
        return this;
    }
}
