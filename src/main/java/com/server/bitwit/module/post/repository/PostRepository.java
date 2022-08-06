package com.server.bitwit.module.post.repository;

import com.server.bitwit.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {
    
    @EntityGraph(attributePaths = "writer")
    Optional<Post> findWithWriterById(@Param("postId") Long postId);
    
    @EntityGraph(attributePaths = {"writer", "stock", "likes", "comments", "comments.writer", "comments.parent", "comments.children", "comments.likes"})
    Optional<Post> findWithAllById(@Param("postId") Long postId);
    
}
