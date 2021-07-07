package com.server.bitwit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
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
    @Column(name = "voting_id")
    Long id;
    
    @ManyToOne
    Stock stock;
    
    @OneToMany
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
}
