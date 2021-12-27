package com.server.bitwit.module.post;

import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.QTag;
import com.server.bitwit.module.post.search.PostSearchCond;
import com.server.bitwit.util.QuerydslRepositoryBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.server.bitwit.domain.QPost.post;
import static com.server.bitwit.domain.QPostLike.postLike;
import static com.server.bitwit.domain.QStock.stock;
import static com.server.bitwit.domain.QTag.tag;
import static com.server.bitwit.util.DynamicQueryUtils.combineWithOr;
import static com.server.bitwit.util.DynamicQueryUtils.contains;
import static com.server.bitwit.util.DynamicQueryUtils.eq;
import static com.server.bitwit.util.DynamicQueryUtils.in;

@Repository
public class PostQueryRepositoryImpl extends QuerydslRepositoryBase implements PostQueryRepository {
    
    protected PostQueryRepositoryImpl( ) {
        super(Post.class);
    }
    
    @Override
    public Page<Post> searchPost(PostSearchCond cond, Pageable pageable) {
        return paginate(pageable,
                selectFrom(post)
                        .distinct( )
                        .leftJoin(post.writer).fetchJoin( )
                        .leftJoin(post.tags, tag)
                        .leftJoin(post.stocks, stock)
                        .leftJoin(post.likes, postLike)
                        .where(
                                in(stock.id, cond.getStockIds( )),
                                in(tag.name, cond.getTags( )),
                                in(stock.ticker, cond.getTickers( )),
                                combineWithOr(
                                        contains(post.title, cond.getKeyword( )),
                                        contains(post.content, cond.getKeyword( ))
                                ),
                                eq(post.writer.name, cond.getWriter( )),
                                eq(postLike.account.id, cond.getLikerId( ))
                        )
        );
    }
    
    @Override
    public Optional<Post> findByIdWithWriterAndStockAndLikes(Long postId) {
        return Optional.ofNullable(
                selectFrom(post)
                        .distinct( )
                        .leftJoin(post.writer).fetchJoin( )
                        .leftJoin(post.stocks)
                        .where(
                                post.id.eq(postId)
                        )
                        .fetchOne( )
        );
    }
}
