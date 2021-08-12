package com.server.bitwit.module.vote;

import com.server.bitwit.module.security.jwt.Jwt;
import com.server.bitwit.module.common.dto.BitwitResponse;
import com.server.bitwit.module.common.dto.SimpleIdResponse;
import com.server.bitwit.module.vote.dto.VoteClientResponse;
import com.server.bitwit.module.vote.dto.VoteRequest;
import com.server.bitwit.module.vote.dto.VoteResponse;
import com.server.bitwit.module.vote.dto.VoteResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/votes")
public class VoteController {
    
    private final VoteService voteService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public BitwitResponse createVote(@Valid @RequestBody VoteRequest voteRequest) {
        var voteId = voteService.createVote(voteRequest);
        return SimpleIdResponse.of(voteId);
    }
    
    @GetMapping("/{voteId}")
    public VoteResponse getVote(@PathVariable Long voteId) {
        return voteService.getVoteResponse(voteId);
    }
    
    @GetMapping("/{voteId}/type/vote-item")
    public VoteResponse getVoteItems(@Jwt Long accountId, @PathVariable Long voteId) {
        return voteService.getVoteItem(voteId, accountId);
    }
    
//    @GetMapping("/{voteId}/type/{responseType}")
    public VoteResponse getVote(@PathVariable Long voteId, @PathVariable VoteResponseType responseType) {
        return voteService.getVoteResponse(voteId, responseType);
    }
    
    @GetMapping
    public List<VoteResponse> getVotes( ) {
        return voteService.getAllVoteResponses( );
    }
    
    @GetMapping("/type/vote-item")
    public List<VoteResponse> getVoteItems(@Jwt Long accountId) {
        return voteService.getVoteItems(accountId);
    }
    
//    @GetMapping("/type/{responseType}")
    public List<VoteResponse> getVotes(@PathVariable VoteResponseType responseType) {
        return voteService.getAllVoteResponses(responseType);
    }
    
    @GetMapping("/status/active")
    public List<VoteClientResponse> getActiveVotes( ) {
        return voteService.getActiveVotes( );
    }
    
    @GetMapping("/me/today")
    public List<VoteClientResponse> getMyVotesParticipatedInToday(@Jwt Long accountId) {
        return voteService.getVotesByAccountIdAndParticipationDate(accountId, LocalDate.now( ));
    }
    
    @GetMapping("/me/{date}")
    public List<VoteClientResponse> getMyVotesByParticipationDate(@Jwt Long accountId, @PathVariable @DateTimeFormat(pattern = "yy-MM-dd") LocalDate date) {
        return voteService.getVotesByAccountIdAndParticipationDate(accountId, date);
    }
    
    @GetMapping("/stock/{stockId}")
    public List<VoteClientResponse> getVotesByStockId(@PathVariable Long stockId) {
        return voteService.getVotesByStockId(stockId);
    }
}
