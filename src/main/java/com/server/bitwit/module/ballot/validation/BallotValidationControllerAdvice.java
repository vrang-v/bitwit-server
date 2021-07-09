package com.server.bitwit.module.ballot.validation;

import com.server.bitwit.module.ballot.BallotController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = BallotController.class)
public class BallotValidationControllerAdvice
{
    private final BallotRequestValidator ballotRequestValidator;
    
    private final UpdateBallotRequestValidator updateBallotRequestValidator;
    
    @InitBinder("ballotRequest")
    public void addBallotRequestValidator(WebDataBinder webDataBinder)
    {
        log.info("webDataBinder = {}", webDataBinder);
        webDataBinder.addValidators(ballotRequestValidator);
    }
    
    @InitBinder("updateBallotRequest")
    public void addBallotUpdateRequestValidator(WebDataBinder webDataBinder)
    {
        webDataBinder.addValidators(updateBallotRequestValidator);
    }
}
