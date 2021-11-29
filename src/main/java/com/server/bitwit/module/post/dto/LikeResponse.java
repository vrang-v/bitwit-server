package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    
    @JsonProperty("likeId")
    Long id;
    
    Long accountId;
    
    boolean like;
    
    public LikeResponse setLike( ) {
        this.like = true;
        return this;
    }
    
    public LikeResponse setDislike( ) {
        this.like = false;
        return this;
    }
}
