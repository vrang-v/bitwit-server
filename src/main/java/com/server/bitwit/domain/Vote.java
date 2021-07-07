package com.server.bitwit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Vote extends BaseTimeEntity
{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "vote_id")
    Long id;
    
    @ManyToOne
    Stock stock;
    
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    List<Ballot> ballots;
    
    @Lob
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    public static Vote onlyId(Long id)
    {
        var vote = new Vote( );
        vote.id = id;
        return vote;
    }
    
    public static Vote createVote(Stock stock, String description, LocalDateTime startAt, LocalDateTime endedAt)
    {
        var vote = new Vote( );
        vote.stock       = stock;
        vote.ballots     = new ArrayList<>( );
        vote.description = description;
        vote.startAt     = startAt;
        vote.endedAt     = endedAt;
        return vote;
    }
    
    public Vote addBallot(Ballot ballot)
    {
        ballots.add(ballot);
        return this;
    }
}
