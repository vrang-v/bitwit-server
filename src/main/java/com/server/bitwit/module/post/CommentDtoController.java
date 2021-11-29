package com.server.bitwit.module.post;

import com.server.bitwit.module.post.dto.CommentWithPostResponse;
import com.server.bitwit.module.post.search.CommentSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentDtoController {
    
    private final CommentService commentService;
    
    @GetMapping("/search/type/with-post")
    public Page<CommentWithPostResponse> search(@ModelAttribute CommentSearchCond cond, Pageable pageable) {
        return commentService.searchComment(cond, pageable, CommentWithPostResponse.class);
    }
    
}
