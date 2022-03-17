package com.server.bitwit;

import com.server.bitwit.domain.Post;
import com.server.bitwit.util.QuerydslRepositoryBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.server.bitwit.domain.QPost.post;
import static com.server.bitwit.domain.QPostLike.postLike;
import static com.server.bitwit.domain.QStock.stock;
import static com.server.bitwit.util.DynamicQueryUtils.eq;

@Repository
public class TestQueryRepository extends QuerydslRepositoryBase {
    
    protected TestQueryRepository( ) {
        super(Post.class);
    }
    
    public Page<Post> searchLikePost(Long accountId, Pageable pageable) {
        return paginate(pageable,
                selectFrom(post)
                        .distinct( )
                        .leftJoin(post.writer).fetchJoin( )
                        .leftJoin(post.stocks, stock)
                        .leftJoin(post.likes, postLike)
                        .leftJoin(post.comments)
                        .where(
                                eq(postLike.account.id, accountId)
                        )
        );
    }
}
