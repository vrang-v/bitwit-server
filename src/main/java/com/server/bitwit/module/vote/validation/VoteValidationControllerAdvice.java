package com.server.bitwit.module.vote.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class VoteValidationControllerAdvice
{
    private final VoteRequestValidator voteRequestValidator;
    
    @InitBinder("voteRequest")
    public void addVoteRequestValidator(WebDataBinder webDataBinder)
    {
        webDataBinder.addValidators(voteRequestValidator);
    }
}
