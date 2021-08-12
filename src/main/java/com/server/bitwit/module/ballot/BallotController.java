package com.server.bitwit.module.ballot;

import com.server.bitwit.module.ballot.dto.CreateOrChangeBallotRequest;
import com.server.bitwit.module.common.dto.BitwitResponse;
import com.server.bitwit.module.security.jwt.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ballots")
public class BallotController {
    
    private final BallotService ballotService;
    
    @PostMapping
    public BitwitResponse createOrChangeBallot(@Jwt Long accountId, @Valid @RequestBody CreateOrChangeBallotRequest request) {
        return ballotService.createOrChangeBallot(accountId, request);
    }
}
