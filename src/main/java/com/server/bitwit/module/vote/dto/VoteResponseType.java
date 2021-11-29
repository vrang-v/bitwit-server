package com.server.bitwit.module.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum VoteResponseType {
    
    DEFAULT("default", VoteDefaultResponse.class),
    MIN("min", VoteMinResponse.class),
    CLIENT("client", VoteClientResponse.class),
    VOTE_ITEM("vote-item", VoteItemResponse.class);
    
    private static final Map<String, VoteResponseType> INSTANCE_STORAGE;
    
    static {
        INSTANCE_STORAGE = new HashMap<>( );
        Arrays.stream(VoteResponseType.values( ))
              .forEach(value -> INSTANCE_STORAGE.put(value.typeName, value));
    }
    
    private final String                        typeName;
    private final Class<? extends VoteResponse> responseType;
    
    public static VoteResponseType findByTypeName(String typeName) {
        return INSTANCE_STORAGE.get(typeName);
    }
}
