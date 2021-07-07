package com.server.bitwit.module.domain;

import lombok.Getter;

@Getter
public enum VotingOption
{
    INCREMENT("increment"), DECREMENT("decrement");
    
    private final String option;
    
    VotingOption(String option)
    {
        this.option = option;
    }
}
