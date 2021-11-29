package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.module.account.dto.AccountResponse;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class CommentResponse {
    
    @JsonProperty("commentId")
    Long id;
    
    String content;
    
    AccountResponse writer;
    
    int depth;
    
    boolean edited;
    
    boolean deleted;
    
    int likeCount;
    
    boolean like;
    
    Instant createdAt;
    
    Long parentId;
    
    List<CommentResponse> children = new ArrayList<>( );
    
    @JsonIgnore
    Set<LikeResponse> likes;
    
    public CommentResponse setLike(Long accountId) {
        like = likes.stream( )
                    .map(LikeResponse::getAccountId)
                    .anyMatch(accountId::equals);
        return this;
    }
}
