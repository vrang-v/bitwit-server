package com.server.bitwit.module.post.repository;

import com.server.bitwit.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
   
    Optional<CommentLike> findByCommentIdAndAccountId(Long commentId, Long accountId);
    
}
