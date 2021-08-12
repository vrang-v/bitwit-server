package com.server.bitwit.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VotingOption
{
    INCREMENT("increment"), DECREMENT("decrement");
    
    private final String option;
}
