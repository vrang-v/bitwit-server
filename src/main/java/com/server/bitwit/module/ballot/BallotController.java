package com.server.bitwit.module.ballot;

import com.server.bitwit.module.ballot.dto.BallotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ballots")
public class BallotController
{
    private final BallotService ballotService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public Long createBallot(@Valid @RequestBody BallotRequest ballotRequest)
    {
        return ballotService.createBallot(ballotRequest);
    }
}
