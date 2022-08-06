package com.server.bitwit.module.post.repository;

import com.server.bitwit.domain.Comment;
import com.server.bitwit.module.post.search.CommentSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentQueryRepository {
    
    Page<Comment> searchComments(CommentSearchCond cond, Pageable pageable);
    
    List<Comment> findByPostId(Long postId);
    
    boolean existParent(Long postId, Long parentId);
}
