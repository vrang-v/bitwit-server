package com.server.bitwit.module.post;

import com.server.bitwit.domain.Comment;
import com.server.bitwit.module.post.search.CommentSearchCond;
import com.server.bitwit.util.QuerydslRepositoryBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.server.bitwit.domain.QComment.comment;
import static com.server.bitwit.domain.QCommentLike.commentLike;
import static com.server.bitwit.util.DynamicQueryUtils.eq;

@Repository
public class CommentQueryRepositoryImpl extends QuerydslRepositoryBase implements CommentQueryRepository {
    
    protected CommentQueryRepositoryImpl( ) {
        super(Comment.class);
    }
    
    @Override
    public Page<Comment> searchComments(CommentSearchCond cond, Pageable pageable) {
        return paginate(pageable,
                selectFrom(comment)
                        .distinct( )
                        .innerJoin(comment.writer).fetchJoin( )
                        .leftJoin(comment.parent).fetchJoin( )
                        .leftJoin(comment.likes, commentLike)
                        .where(
                                eq(comment.writer.name, cond.getWriterName( )),
                                eq(comment.writer.id, cond.getWriterId( )),
                                eq(commentLike.account.id, cond.getLikerId( ))
                        )
        );
    }
    
    @Override
    public List<Comment> findByPostId(Long postId) {
        return selectFrom(comment)
                .innerJoin(comment.writer).fetchJoin( )
                .leftJoin(comment.parent).fetchJoin( )
                .where(comment.post.id.eq(postId))
                .orderBy(
                        comment.parent.id.asc( ).nullsFirst( ),
                        comment.createdAt.asc( )
                )
                .fetch( );
    }
    
    @Override
    public boolean existParent(Long postId, Long parentId) {
        return selectFrom(comment)
                       .leftJoin(comment.parent)
                       .leftJoin(comment.post)
                       .where(
                               comment.post.id.eq(postId),
                               comment.id.eq(parentId)
                       )
                       .fetchCount( ) != 0;
    }
}
