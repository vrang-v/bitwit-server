package com.server.bitwit.domain;

import com.server.bitwit.module.common.converter.VotingOptionIntegerMapJsonConverter;
import com.server.bitwit.module.error.exception.InvalidRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Getter @FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Vote extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "vote_id")
    Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    Stock stock;
    
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    List<Ballot> ballots;
    
    @Column(columnDefinition="TEXT")
    String description;
    
    LocalDateTime startAt;
    
    LocalDateTime endedAt;
    
    int participantsCount;
    
    @Lob
    @Convert(converter = VotingOptionIntegerMapJsonConverter.class)
    Map<VotingOption, Integer> selectionCount;
    
    int views;
    
    public static Vote createVote(Stock stock, String description, LocalDateTime startAt, LocalDateTime endedAt) {
        verifyValidPeriod(startAt, endedAt);
        var vote = new Vote( );
        vote.stock          = stock;
        vote.ballots        = new ArrayList<>( );
        vote.description    = description;
        vote.startAt        = startAt;
        vote.endedAt        = endedAt;
        vote.selectionCount = new LinkedHashMap<>( );
        return vote;
    }
    
    private static void verifyValidPeriod(LocalDateTime startAt, LocalDateTime endedAt) {
        if (startAt.isAfter(endedAt)) {
            throw new InvalidRequestException("startAt이 endedAt 보다 늦게 설정되어 있습니다.");
        }
    }
    
    public boolean isActive( ) {
        var now = LocalDateTime.now( );
        return now.isAfter(startAt) && now.isBefore(endedAt);
    }
    
    public Vote addBallot(VotingOption votingOption) {
        validateActivePeriod( );
        selectionCount.compute(votingOption, (unused, count) -> count != null ? count + 1 : 1);
        participantsCount += 1;
        return this;
    }
    
    public Vote changeBallot(VotingOption previousOption, VotingOption votingOption) {
        validateActivePeriod( );
        selectionCount.compute(previousOption, (unused, count) -> {
            if (count == null) {
                throw new InvalidRequestException("변경할 수 있는 투표용지가 없습니다.");
            }
            return count - 1;
        });
        selectionCount.compute(votingOption, (unused, count) -> count != null ? count + 1 : 1);
        return this;
    }
    
    public Vote removeBallot(VotingOption votingOption) {
        validateActivePeriod( );
        selectionCount.compute(votingOption, (unused, count) -> {
            if (count == null) {
                throw new InvalidRequestException("삭제할 수 있는 투표용지가 없습니다.");
            }
            return count - 1;
        });
        participantsCount -= 1;
        return this;
    }
    
    private void validateActivePeriod( ) {
        if (! isActive( )) {
            throw new InvalidRequestException("투표 참여기간이 아닙니다.");
        }
    }
    
    public Vote hideBallots( ) {
        ballots = null;
        return this;
    }
}
