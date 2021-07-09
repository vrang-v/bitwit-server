package com.server.bitwit.module.ballot;

import com.server.bitwit.module.ballot.dto.BallotRequest;
import com.server.bitwit.module.ballot.dto.UpdateBallotRequest;
import com.server.bitwit.module.common.BitwitResponse;
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
    public BitwitResponse createBallot(@Valid @RequestBody BallotRequest request)
    {
        return ballotService.createBallot(request);
    }
    
    @PatchMapping("/{ballotId}")
    public void updateBallot(@PathVariable Long ballotId, @Valid @RequestBody UpdateBallotRequest request)
    {
        ballotService.updateBallot(ballotId, request);
    }
    
    @DeleteMapping("/{ballotId}")
    public void deleteBallot(@PathVariable Long ballotId)
    {
        ballotService.deleteBallot(ballotId);
    }
}
