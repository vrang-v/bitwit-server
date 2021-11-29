package com.server.bitwit.module.post;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    Optional<PostLike> findByPostAndAccount(Post post, Account account);
    
    Optional<PostLike> findByPostIdAndAccountId(Long postId, Long accountId);
    
}
