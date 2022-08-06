package com.server.bitwit.module.post.service;

import com.server.bitwit.domain.Account;
import com.server.bitwit.domain.Comment;
import com.server.bitwit.domain.CommentLike;
import com.server.bitwit.module.common.service.MappingService;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.error.exception.InvalidRequestException;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.post.dto.*;
import com.server.bitwit.module.post.repository.CommentLikeRepository;
import com.server.bitwit.module.post.repository.CommentRepository;
import com.server.bitwit.module.post.search.CommentSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    
    private final CommentRepository     commentRepository;
    private final CommentLikeRepository likeRepository;
    
    private final MappingService mappingService;
    
    @Transactional
    public CommentResponse createComment(CreateCommentRequest request) {
        return Optional.ofNullable(mappingService.mapTo(request, Comment.class))
                       .map(commentRepository::save)
                       .map(comment -> mappingService.mapTo(comment, CommentResponse.class))
                       .orElseThrow(( ) -> new InvalidRequestException("댓글을 생성할 수 없습니다."));
    }
    
    public Page<CommentResponse> searchComment(CommentSearchCond cond, Pageable pageable) {
        return commentRepository.searchComments(cond, pageable)
                                .map(comment -> mappingService.mapTo(comment, CommentResponse.class));
    }
    
    public <T> Page<T> searchComment(CommentSearchCond cond, Pageable pageable, Class<T> responseType) {
        return commentRepository.searchComments(cond, pageable)
                                .map(comment -> mappingService.mapTo(comment, responseType));
    }
    
    public FullCommentResponse findById(Long id) {
        var comment = commentRepository.findById(id).orElseThrow(BitwitException::new);
        return mappingService.mapTo(comment, FullCommentResponse.class);
    }
    
    public List<CommentResponse> findAllByPostId(Long postId, Long accountId) {
        var comments = commentRepository.findByPostId(postId);
        return convertToNestedComments(comments, accountId);
    }
    
    @Transactional
    public void updateComment(UpdateCommentRequest request) {
        var accountId = request.getAccountId( );
        var comment   = mappingService.mapTo(request.getCommentId( ), Comment.class);
        if (! comment.getWriter( ).getId( ).equals(accountId)) {
            throw new BitwitException(ErrorCode.NO_PERMISSION);
        }
        comment.updateContent(request.getContent( ));
    }
    
    @Transactional
    public void deleteComment(Long commentId, Long accountId) {
        var comment = mappingService.mapTo(commentId, Comment.class);
        
        if (! comment.getWriter( ).getId( ).equals(accountId)) {
            throw new BitwitException(ErrorCode.NO_PERMISSION);
        }
        
        if (comment.isDeleted( )) {
            throw new InvalidRequestException("이미 삭제된 댓글입니다.");
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
            var response = mappingService.mapTo(comment, CommentResponse.class);
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
            return mappingService.mapTo(like.get( ), LikeResponse.class)
                                 .setLike( );
        }
        var comment = mappingService.mapTo(commentId, Comment.class);
        var account = mappingService.mapTo(accountId, Account.class);
        var newLike = likeRepository.save(new CommentLike(comment, account));
        return mappingService.mapTo(newLike, LikeResponse.class)
                             .setLike( );
    }
    
    public LikeResponse dislike(Long commentId, Long accountId) {
        var like = likeRepository.findByCommentIdAndAccountId(commentId, accountId)
                                 .orElseThrow(( ) -> new NonExistentResourceException("like"));
        likeRepository.delete(like);
        return mappingService.mapTo(like, LikeResponse.class)
                             .setDislike( );
    }
}
