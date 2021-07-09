package com.server.bitwit.module.domain;

import com.server.bitwit.infra.exception.BitwitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoteTest
{
    @Test
    @DisplayName("Vote 생성 / 정상")
    void createVote_normal( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2021, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(2022, 1, 1, 0, 0);
        
        // when
        var vote = Vote.createVote(stock, description, startTime, endTime);
        
        // then
        assertThat(vote.getStock( )).isEqualTo(stock);
        assertThat(vote.getDescription( )).isEqualTo(description);
        assertThat(vote.getStartAt( )).isEqualTo(startTime);
        assertThat(vote.getEndedAt( )).isEqualTo(endTime);
        assertThat(vote.getBallots( )).isEmpty( );
    }
    
    @Test
    @DisplayName("Vote 생성 / 시작 시간이 끝나는 시간 보다 뒤")
    void createVote_startTimeIsLaterThanEndTime_exception( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2022, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(2021, 1, 1, 0, 0);
        
        // when, then
        assertThrows(BitwitException.class, ( ) -> Vote.createVote(stock, description, startTime, endTime));
    }
    
    @Test
    @DisplayName("Ballot 추가 / 정상")
    void addBallot_normal( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2021, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(3021, 1, 1, 0, 0);
        var vote        = Vote.createVote(stock, description, startTime, endTime);
        
        // when
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.DECREMENT);
        
        // then
        assertThat(vote.getParticipantsCount( )).isEqualTo(3);
        assertThat(vote.getSelectionsCount( )).containsExactly(
                entry(VotingOption.INCREMENT, 2),
                entry(VotingOption.DECREMENT, 1)
        );
    }
    
    @Test
    @DisplayName("Ballot 추가 / 투표 기간 종료")
    void addBallot_votingPeriodEnd( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2000, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(2020, 1, 1, 0, 0);
        var vote        = Vote.createVote(stock, description, startTime, endTime);
        
        // when
        assertThrows(BitwitException.class, ( ) -> vote.addBallot(VotingOption.INCREMENT));
        assertThrows(BitwitException.class, ( ) -> vote.addBallot(VotingOption.DECREMENT));
        assertThat(vote.getParticipantsCount( )).isZero( );
        assertThat(vote.getSelectionsCount( )).isEmpty( );
    }
    
    @Test
    @DisplayName("Ballot 수정 / 정상")
    void changeBallot_normal( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2021, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(3021, 1, 1, 0, 0);
        var vote        = Vote.createVote(stock, description, startTime, endTime);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.DECREMENT);
        
        // when
        vote.changeBallot(VotingOption.INCREMENT, VotingOption.DECREMENT);
        
        // then
        assertThat(vote.getParticipantsCount( )).isEqualTo(3);
        assertThat(vote.getSelectionsCount( )).containsExactly(
                entry(VotingOption.INCREMENT, 1),
                entry(VotingOption.DECREMENT, 2)
        );
    }
    
    @Test
    @DisplayName("Ballot 수정 / 투표 기간 종료")
    void changeBallot_votingPeriodEnd( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2021, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(3021, 1, 1, 0, 0);
        var vote        = Vote.createVote(stock, description, startTime, endTime);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.DECREMENT);
        
        // when
        vote.changeBallot(VotingOption.INCREMENT, VotingOption.DECREMENT);
        
        // then
        assertThat(vote.getParticipantsCount( )).isEqualTo(3);
        assertThat(vote.getSelectionsCount( )).containsExactly(
                entry(VotingOption.INCREMENT, 1),
                entry(VotingOption.DECREMENT, 2)
        );
    }
    
    @Test
    @DisplayName("Ballot 수정 / 수량 0인 항목을 변경")
    void changeBallot_changeCount0Option( )
    {
        // TODO: 투표 유효 기간에 참여한 항목을 투표 기간이 종료되고 항목을 변경하는 테스트
    }
    
    @Test
    @DisplayName("Ballot 삭제 / 정상")
    void removeBallot_normal( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2021, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(3021, 1, 1, 0, 0);
        var vote        = Vote.createVote(stock, description, startTime, endTime);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.DECREMENT);
        
        // when
        vote.removeBallot(VotingOption.INCREMENT);
        
        // then
        assertThat(vote.getParticipantsCount( )).isEqualTo(2);
        assertThat(vote.getSelectionsCount( )).containsExactly(
                entry(VotingOption.INCREMENT, 1),
                entry(VotingOption.DECREMENT, 1)
        );
    }
    
    @Test
    @DisplayName("Ballot 삭제 / 수량 0인 항목을 삭제")
    void removeBallot_removeCount0Option( )
    {
        // given
        var stock       = Stock.createStock("AAPL");
        var description = "test description";
        var startTime   = LocalDateTime.of(2021, 1, 1, 0, 0);
        var endTime     = LocalDateTime.of(3021, 1, 1, 0, 0);
        var vote        = Vote.createVote(stock, description, startTime, endTime);
        vote.addBallot(VotingOption.INCREMENT);
        vote.addBallot(VotingOption.INCREMENT);
        
        // when
        assertThrows(BitwitException.class, ( ) -> vote.removeBallot(VotingOption.DECREMENT));
        
        // then
        assertThat(vote.getParticipantsCount( )).isEqualTo(2);
        assertThat(vote.getSelectionsCount( )).containsExactly(entry(VotingOption.INCREMENT, 2));
    }
}
