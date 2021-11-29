package com.server.bitwit.module.post.dto;

import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class CreateCommentRequest {
    
    @Nullable
    Long parentId;
    
    @Null
    Long accountId;
    
    @NotNull
    Long postId;
    
    @NotBlank
    String content;
    
    public CreateCommentRequest withAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }
}
