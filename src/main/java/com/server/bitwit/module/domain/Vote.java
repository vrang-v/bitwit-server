package com.server.bitwit.module.domain;

import com.server.bitwit.infra.exception.BitwitException;
import com.server.bitwit.module.common.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Slf4j
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
    
    int participantsCount;
    
    @ElementCollection(fetch = FetchType.EAGER)
    Map<VotingOption, Integer> selectionsCount;
    
    public static Vote createVote(Stock stock, String description, LocalDateTime startAt, LocalDateTime endedAt)
    {
        verifyValidPeriod(startAt, endedAt);
        var vote = new Vote( );
        vote.stock           = stock;
        vote.ballots         = new ArrayList<>( );
        vote.description     = description;
        vote.startAt         = startAt;
        vote.endedAt         = endedAt;
        vote.selectionsCount = new EnumMap<>(VotingOption.class);
        return vote;
    }
    
    private static void verifyValidPeriod(LocalDateTime startAt, LocalDateTime endedAt)
    {
        if (startAt.isAfter(endedAt)) {
            throw new BitwitException("검증되지 않은 start At과 endedAt이 사용됩니다.");
        }
    }
    
    public boolean isActive( )
    {
        var now = LocalDateTime.now( );
        return now.isAfter(startAt) && now.isBefore(endedAt);
    }
    
    public Vote addBallot(VotingOption votingOption)
    {
        validateActivePeriod( );
        selectionsCount.compute(votingOption, (unused, count) -> count != null ? count + 1 : 1);
        participantsCount += 1;
        return this;
    }
    
    public Vote changeBallot(VotingOption previousOption, VotingOption votingOption)
    {
        validateActivePeriod( );
        selectionsCount.compute(previousOption, (unused, count) -> {
            if (count == null) {
                throw new BitwitException("변경할 수 있는 투표용지가 없습니다.");
            }
            return count - 1;
        });
        selectionsCount.compute(votingOption, (unused, count) -> count != null ? count + 1 : 1);
        return this;
    }
    
    public Vote removeBallot(VotingOption votingOption)
    {
        validateActivePeriod( );
        selectionsCount.compute(votingOption, (unused, count) -> {
            if (count == null) {
                throw new BitwitException("삭제할 수 있는 투표용지가 없습니다.");
            }
            return count - 1;
        });
        participantsCount -= 1;
        return this;
    }
    
    private void validateActivePeriod( )
    {
        if (! isActive( )) {
            throw new BitwitException("투표 참여기간이 아닙니다.");
        }
    }
}
