package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@JsonInclude(Include.NON_NULL)
public class UpdateCommentRequest {
    
    @NotBlank
    private String content;
    
    @Null(message = "AccountId는 지정할 수 없습니다. JWT 토큰에서 가져옵니다.")
    private Long accountId;
    
    @Null(message = "CommentId는 지정할 수 없습니다. URL Path에서 가져옵니다.")
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
