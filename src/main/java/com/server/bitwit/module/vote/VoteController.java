package com.server.bitwit.module.vote;

import com.server.bitwit.module.vote.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/votes")
public class VoteController
{
    private final VoteService voteService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public void createVote(@Valid @RequestBody VoteRequest voteRequest)
    {
        voteService.createVote(voteRequest);
    }
    
    @GetMapping("/{voteId}")
    public VoteResponse getVote(@PathVariable Long voteId)
    {
        return voteService.getVoteResponse(voteId);
    }
    
    @GetMapping("/{voteId}/type/{responseType}")
    public VoteResponse getVote(@PathVariable Long voteId, @PathVariable VoteResponseType responseType)
    {
        return voteService.getVoteResponse(voteId, responseType);
    }
    
    @GetMapping
    public List<VoteResponse> getVotes( )
    {
        return voteService.getAllVoteResponses( );
    }
    
    @GetMapping("/type/{responseType}")
    public List<VoteResponse> getVotes(@PathVariable VoteResponseType responseType)
    {
        return voteService.getAllVoteResponses(responseType);
    }
}
