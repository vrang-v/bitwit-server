package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.stock.dto.StockResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PostResponse {
    
    @JsonProperty("postId")
    Long id;
    
    String title;
    
    String content;
    
    int viewCount;
    
    AccountResponse writer;
    
    Set<StockResponse> stocks;
    
    int likeCount;
    
    boolean like;
    
    Instant createdAt;
    
    List<CommentResponse> comments;
    
    int commentCount;
    
    @JsonIgnore
    Set<LikeResponse> likes;
    
    public PostResponse setLike(Long accountId) {
        like = likes.stream( )
                    .map(LikeResponse::getAccountId)
                    .anyMatch(accountId::equals);
        return this;
    }
    
    public PostResponse setComments(List<CommentResponse> comments) {
        this.comments = comments;
        return this;
    }
}
