package com.server.bitwit.module.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdatePostRequest {
    
    String title;
    
    String content;
    
    long accountId;
    
    Long stockId;
    
    List<String> tags;
    
    List<String> tickers;
    
    public UpdatePostRequest withAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }
    
    public void distinctTickers( ) {
        tickers = tickers.stream( ).distinct( ).toList( );
    }
}
