package com.server.bitwit.module.post;

import com.server.bitwit.module.post.dto.CommentResponse;
import com.server.bitwit.module.post.dto.CreateCommentRequest;
import com.server.bitwit.module.post.dto.LikeResponse;
import com.server.bitwit.module.post.dto.UpdateCommentRequest;
import com.server.bitwit.module.post.search.CommentSearchCond;
import com.server.bitwit.module.security.jwt.support.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public CommentResponse createComment(@Jwt Long accountId, @Valid @RequestBody CreateCommentRequest request) {
        return commentService.createComment(request.withAccountId(accountId));
    }
    
    @GetMapping("/search")
    public Page<CommentResponse> search(@ModelAttribute CommentSearchCond cond, Pageable pageable) {
        return commentService.searchComment(cond, pageable);
    }
    
    @GetMapping("/{commentId}")
    public CommentResponse getComment(@Jwt Long accountId, @PathVariable Long commentId) {
        return null;
    }
    
    @PatchMapping("/{commentId}")
    public void updateComment(@Jwt Long accountId, @PathVariable Long commentId, @Valid @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(request.withCommentId(commentId).withAccountId(accountId));
    }
    
    @DeleteMapping("/{commentId}")
    public void deleteComment(@Jwt Long accountId, @PathVariable Long commentId) {
        
        commentService.deleteComment(commentId, accountId);
    }
    
    @PostMapping("/{commentId}/like")
    public LikeResponse like(@Jwt Long accountId, @PathVariable Long commentId) {
        return commentService.like(commentId, accountId);
    }
    
    @DeleteMapping("/{commentId}/like")
    public LikeResponse dislike(@Jwt Long accountId, @PathVariable Long commentId) {
        return commentService.dislike(commentId, accountId);
    }
}
