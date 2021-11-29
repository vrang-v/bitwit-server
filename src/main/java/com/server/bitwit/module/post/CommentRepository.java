package com.server.bitwit.module.post;

import com.server.bitwit.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {
    
    @Override
    Optional<Comment> findById(Long aLong);
}
