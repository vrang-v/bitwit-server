package com.server.bitwit.controller;

import com.server.bitwit.dto.request.AccountRequest;
import com.server.bitwit.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController
{
    private final AccountService accountService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public void createAccount(@Valid @RequestBody AccountRequest request)
    {
        accountService.createAccount(request);
    }
}
