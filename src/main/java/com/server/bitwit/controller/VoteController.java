package com.server.bitwit.controller;

import com.server.bitwit.dto.request.VoteRequest;
import com.server.bitwit.dto.response.VoteResponse;
import com.server.bitwit.service.VoteService;
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
        return voteService.getVote(voteId);
    }
    
    @GetMapping
    public List<VoteResponse> getVotes( )
    {
        return voteService.getAllVotes( );
    }
}
