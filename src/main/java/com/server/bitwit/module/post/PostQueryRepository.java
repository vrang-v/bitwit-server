package com.server.bitwit.module.post;

import com.server.bitwit.domain.Post;
import com.server.bitwit.module.post.search.PostSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostQueryRepository {
    
    Page<Post> searchPost(PostSearchCond cond, Pageable pageable);
    
    Optional<Post> findByIdWithWriterAndStockAndLikes(Long postId);
}
