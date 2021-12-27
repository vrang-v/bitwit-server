package com.server.bitwit.module.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CreatePostRequest {
    
    @NotBlank
    String title;
    
    @NotBlank
    String content;
    
    long accountId;
    
    List<String> tickers;
    
    List<String> tags;
    
    public CreatePostRequest withAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }
    
    public void distinctTickers( ) {
        tickers = tickers.stream( ).distinct( ).collect(Collectors.toList( ));
    }
}
