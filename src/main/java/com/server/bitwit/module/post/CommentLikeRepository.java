package com.server.bitwit.module.post;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Comment;
import com.server.bitwit.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    
    Optional<CommentLike> findByCommentAndAccount(Comment comment, Account account);
    
    Optional<CommentLike> findByCommentIdAndAccountId(Long commentId, Long accountId);
    
}
