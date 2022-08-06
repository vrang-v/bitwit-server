package com.server.bitwit.module.ballot;

import com.server.bitwit.module.ballot.dto.BallotResponse;
import com.server.bitwit.module.ballot.dto.CreateOrChangeBallotRequest;
import com.server.bitwit.module.security.jwt.support.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ballots")
public class BallotController {
    
    private final BallotService ballotService;
    
    @PostMapping
    public ResponseEntity<BallotResponse> createOrChangeBallot(@Jwt Long accountId, @Valid @RequestBody CreateOrChangeBallotRequest request) {
        var response = ballotService.createOrChangeBallot(accountId, request);
        return ResponseEntity.status(response.getStatus( ).equals("CREATED") ? CREATED : OK)
                             .body(response);
    }
}
