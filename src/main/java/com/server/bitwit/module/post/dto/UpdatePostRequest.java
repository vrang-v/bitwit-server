package com.server.bitwit.module.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UpdatePostRequest {
    
    String title;
    
    String content;
    
    long accountId;
    
    Long stockId;
    
    List<String> tickers;
    
    public UpdatePostRequest withAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }
    
    public void distinctTickers( ) {
        tickers = tickers.stream( ).distinct( ).collect(Collectors.toList( ));
    }
}
