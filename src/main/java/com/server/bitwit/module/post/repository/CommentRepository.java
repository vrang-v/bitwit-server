package com.server.bitwit.module.post.repository;

import com.server.bitwit.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryRepository {

}
