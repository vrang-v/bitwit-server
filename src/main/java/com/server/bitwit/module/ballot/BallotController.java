package com.server.bitwit.module.ballot;

import com.server.bitwit.module.ballot.dto.BallotRequest;
import com.server.bitwit.module.ballot.validator.BallotRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ballots")
public class BallotController
{
    private final BallotService ballotService;
    
    private final BallotRequestValidator ballotRequestValidator;
    
    @InitBinder("ballotRequest")
    public void addBallotRequestValidator(WebDataBinder webDataBinder)
    {
        webDataBinder.addValidators(ballotRequestValidator);
    }
    
    @PostMapping
    @ResponseStatus(CREATED)
    public Long createBallot(@Valid @RequestBody BallotRequest ballotRequest)
    {
        return ballotService.createBallot(ballotRequest);
    }
}
