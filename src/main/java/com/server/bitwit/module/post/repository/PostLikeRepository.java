package com.server.bitwit.module.post.repository;

import com.server.bitwit.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    Optional<PostLike> findByPostIdAndAccountId(Long postId, Long accountId);
    
}
