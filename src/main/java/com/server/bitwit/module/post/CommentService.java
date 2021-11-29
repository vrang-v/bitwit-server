package com.server.bitwit.module.post;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Comment;
import com.server.bitwit.domain.CommentLike;
import com.server.bitwit.domain.Post;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.post.dto.CommentResponse;
import com.server.bitwit.module.post.dto.CreateCommentRequest;
import com.server.bitwit.module.post.dto.LikeResponse;
import com.server.bitwit.module.post.dto.UpdateCommentRequest;
import com.server.bitwit.module.post.search.CommentSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    
    private final CommentRepository     commentRepository;
    private final CommentLikeRepository likeRepository;
    
    private final ConversionService conversionService;
    
    public CommentResponse createComment(CreateCommentRequest request) {
        Comment parent = null;
        if (request.getParentId( ) != null) {
            if (! commentRepository.existParent(request.getPostId( ), request.getParentId( ))) {
                throw new BitwitException(ErrorCode.INVALID_REQUEST);
            }
            parent = conversionService.convert(request.getParentId( ), Comment.class);
        }
        var account = conversionService.convert(request.getAccountId( ), Account.class);
        var post    = conversionService.convert(request.getPostId( ), Post.class);
        var comment = commentRepository.save(new Comment(request.getContent( ), account, post, parent));
        return conversionService.convert(comment, CommentResponse.class);
    }
    
    public Page<CommentResponse> searchComment(CommentSearchCond cond, Pageable pageable) {
        return commentRepository.searchComments(cond, pageable)
                                .map(comment -> conversionService.convert(comment, CommentResponse.class));
    }
    
    public <T> Page<T> searchComment(CommentSearchCond cond, Pageable pageable, Class<T> responseType) {
        return commentRepository.searchComments(cond, pageable)
                                .map(comment -> conversionService.convert(comment, responseType));
    }
    
    public List<CommentResponse> findAllByPostId(Long postId, Long accountId) {
        var comments = commentRepository.findByPostId(postId);
        return convertToNestedComments(comments, accountId);
    }
    
    @Transactional
    public void updateComment(UpdateCommentRequest request) {
        var accountId = request.getAccountId( );
        var comment   = conversionService.convert(request.getCommentId( ), Comment.class);
        if (! comment.getWriter( ).getId( ).equals(accountId)) {
            throw new BitwitException(ErrorCode.NO_PERMISSION);
        }
        comment.updateContent(request.getContent( ));
    }
    
    @Transactional
    public void deleteComment(Long commentId, Long accountId) {
        var comment = commentRepository.findById(commentId)
                                       .orElseThrow(( ) -> new NonExistentResourceException("comment", commentId));
        if (! comment.getWriter( ).getId( ).equals(accountId)) {
            throw new BitwitException(ErrorCode.NO_PERMISSION);
        }
        
        if (comment.getChildren( ).isEmpty( )) {
            var deletableTopLevelComment = comment.getDeletableTopLevelComment( );
            commentRepository.delete(deletableTopLevelComment);
        }
        else {
            comment.delete( );
        }
    }
    
    public List<CommentResponse> convertToNestedComments(List<Comment> comments, Long accountId) {
        var responses = new ArrayList<CommentResponse>( );
        var memory    = new HashMap<Long, CommentResponse>( );
        comments.forEach(comment -> {
            var response = conversionService.convert(comment, CommentResponse.class);
            response.setLike(accountId);
            memory.put(response.getId( ), response);
            if (comment.isRoot( )) {
                responses.add(response);
            }
            else {
                memory.get(comment.getParent( ).getId( )).getChildren( ).add(response);
            }
        });
        return responses;
    }
    
    public LikeResponse like(Long commentId, Long accountId) {
        var like = likeRepository.findByCommentIdAndAccountId(commentId, accountId);
        if (like.isPresent( )) {
            return conversionService.convert(like.get( ), LikeResponse.class).setLike( );
        }
        var comment = conversionService.convert(commentId, Comment.class);
        var account = conversionService.convert(accountId, Account.class);
        var newLike = likeRepository.save(new CommentLike(comment, account));
        return conversionService.convert(newLike, LikeResponse.class).setLike( );
    }
    
    public LikeResponse dislike(Long commentId, Long accountId) {
        var like = likeRepository.findByCommentIdAndAccountId(commentId, accountId);
        like.ifPresent(likeRepository::delete);
        return conversionService.convert(like.get( ), LikeResponse.class)
                                .setDislike( );
    }
}
