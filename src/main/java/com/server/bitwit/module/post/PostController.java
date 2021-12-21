package com.server.bitwit.module.post;

import com.server.bitwit.module.post.dto.*;
import com.server.bitwit.module.post.search.PostSearchCond;
import com.server.bitwit.module.security.jwt.support.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private final PostService    postService;
    private final CommentService commentService;
    
    @PostMapping
    @ResponseStatus(CREATED)
    public PostResponse createPost(@Jwt Long accountId, @Valid @RequestBody CreatePostRequest request) {
        request.distinctTickers( );
        return postService.createPost(request.withAccountId(accountId));
    }
    
    @GetMapping("/search")
    public List<PostResponse> search(@Jwt Long accountId, @ModelAttribute PostSearchCond cond, Pageable pageable) {
        return postService.searchPosts(accountId, cond, pageable);
    }
    
    @GetMapping("/{postId}")
    public PostResponse getPost(@Jwt Long accountId, @PathVariable Long postId) {
        return postService.getPost(postId, accountId);
    }
    
    @PatchMapping("/{postId}")
    public PostResponse updatePost(@Jwt Long accountId, @PathVariable Long postId, @Valid @RequestBody UpdatePostRequest request) {
        return postService.updatePost(postId, request.withAccountId(accountId));
    }
    
    @DeleteMapping("/{postId}")
    public void deletePost(@Jwt Long accountId, @PathVariable Long postId) {
        postService.deletePost(postId, accountId);
    }
    
    @GetMapping("/{postId}/view")
    public PostResponse viewPost(@Jwt Long accountId, @PathVariable Long postId) {
        return postService.viewPost(postId, accountId);
    }
    
    @GetMapping("/{postId}/comments")
    public List<CommentResponse> getCommentsOnPost(@Jwt Long accountId, @PathVariable Long postId) {
        return commentService.findAllByPostId(postId, accountId);
    }
    
    @PostMapping("/{postId}/like")
    public LikeResponse like(@Jwt Long accountId, @PathVariable Long postId) {
        return postService.like(postId, accountId);
    }
    
    @DeleteMapping("/{postId}/like")
    public LikeResponse dislike(@Jwt Long accountId, @PathVariable Long postId) {
        return postService.dislike(postId, accountId);
    }
}
