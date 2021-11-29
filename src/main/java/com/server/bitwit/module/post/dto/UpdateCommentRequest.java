package com.server.bitwit.module.post.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
public class UpdateCommentRequest {
    
    @NotBlank
    private String content;
    
    @Null
    private Long accountId;
    
    @Null
    private Long commentId;
    
    public UpdateCommentRequest withAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }
    
    public UpdateCommentRequest withCommentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }
}
