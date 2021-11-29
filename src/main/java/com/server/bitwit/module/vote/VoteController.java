package com.server.bitwit.module.vote;

import com.server.bitwit.module.security.jwt.support.Jwt;
import com.server.bitwit.module.vote.dto.*;
import com.server.bitwit.module.vote.search.VoteSearchCond;
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
@RequestMapping("/api/votes")
public class VoteController {
    
    private final VoteService voteService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public VoteItemResponse createVote(@Valid @RequestBody CreateVoteRequest createVoteRequest) {
        return voteService.createVote(createVoteRequest);
    }
    
    @GetMapping("/search/type/{responseType}")
    public List<? extends VoteResponse> searchVotes(@Jwt Long accountId, @ModelAttribute VoteSearchCond cond, @PathVariable VoteResponseType responseType) {
        return voteService.searchVotes(cond.withAccountId(accountId), responseType.getResponseType( ));
    }
    
    @GetMapping("/{voteId}")
    public VoteDefaultResponse getVote(@Jwt Long accountId, @PathVariable Long voteId) {
        var cond = VoteSearchCond.builder( ).accountId(accountId).voteIds(List.of(voteId)).build( );
        return voteService.searchVote(cond, VoteDefaultResponse.class);
    }
    
    @GetMapping
    public List<VoteDefaultResponse> getVotes(@Jwt Long accountId) {
        return voteService.searchVotes(new VoteSearchCond( ).withAccountId(accountId), VoteDefaultResponse.class);
    }
    
    @GetMapping("/{voteId}/type/{responseType}")
    public VoteResponse getVote(@Jwt Long accountId, @PathVariable Long voteId, @PathVariable VoteResponseType responseType) {
        var cond = VoteSearchCond.builder( ).accountId(accountId).voteIds(List.of(voteId)).build( );
        return voteService.searchVote(cond, responseType.getResponseType( ));
    }
    
    @GetMapping("/type/{responseType}")
    public List<? extends VoteResponse> getVotes(@Jwt Long accountId, @PathVariable VoteResponseType responseType) {
        return voteService.searchVotes(new VoteSearchCond( ).withAccountId(accountId), responseType.getResponseType( ));
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
}
