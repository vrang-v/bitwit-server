package com.server.bitwit.module.ballot.validation;

import com.server.bitwit.module.ballot.BallotController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = BallotController.class)
public class BallotValidationControllerAdvice
{
    private final BallotRequestValidator ballotRequestValidator;
    
    @InitBinder("ballotRequest")
    public void addBallotRequestValidator(WebDataBinder webDataBinder)
    {
        webDataBinder.addValidators(ballotRequestValidator);
    }
}
