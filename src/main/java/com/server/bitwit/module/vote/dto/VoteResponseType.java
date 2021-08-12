package com.server.bitwit.module.vote.dto;

import com.server.bitwit.domain.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum VoteResponseType
{
    DEFAULT("default", VoteDefaultResponse::fromVote),
    MIN("min", VoteMinResponse::fromVote),
    CLIENT("client", VoteClientResponse::fromVote);
    
    private static final Map<String, VoteResponseType> INSTANCE_STORAGE;
    
    static {
        INSTANCE_STORAGE = new HashMap<>( );
        Arrays.stream(VoteResponseType.values( ))
              .forEach(value -> INSTANCE_STORAGE.put(value.typeName, value));
    }
    
    private final String                        typeName;
    private final Converter<Vote, VoteResponse> converter;
    
    public static VoteResponseType findByTypeName(String typeName)
    {
        return INSTANCE_STORAGE.get(typeName);
    }
    
    public VoteResponse fromVote(Vote vote)
    {
        return this.converter.convert(vote);
    }
}
