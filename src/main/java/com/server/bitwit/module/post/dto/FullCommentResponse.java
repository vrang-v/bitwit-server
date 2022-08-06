package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Comment;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.account.dto.AccountResponse.AccountResponseMapper;
import com.server.bitwit.module.post.dto.LikeResponse.CommentLikeResponseMapper;
import com.server.bitwit.module.post.service.CommentService;
import lombok.Data;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class FullCommentResponse {
    
    @JsonProperty("commentId")
    Long id;
    
    Long postId;
    
    String content;
    
    AccountResponse writer;
    
    int depth;
    
    boolean edited;
    
    boolean deleted;
    
    int likeCount;
    
    boolean like;
    
    Instant createdAt;
    
    Long parentId;
    
    List<FullCommentResponse> children = new ArrayList<>( );
    
    @JsonIgnore
    Set<LikeResponse> likes;
    
    public FullCommentResponse setLike(Long accountId) {
        like = likes.stream( )
                    .map(LikeResponse::getAccountId)
                    .anyMatch(accountId::equals);
        return this;
    }
    
    
    @Mapper(config = MapStructConfig.class, uses = {AccountResponseMapper.class, CommentLikeResponseMapper.class})
    public abstract static class CommentResponseMapper implements Converter<Comment, FullCommentResponse> {
        
        @Lazy @Autowired CommentService commentService;
        
        @Override
        @Mapping(target = "postId", source = "post.id")
        @Mapping(target = "parentId", source = "parent.id")
        @Mapping(target = "likeCount", expression = "java(comment.getLikes().size())")
        public abstract FullCommentResponse convert(Comment comment);
        
        @AfterMapping
        void afterMapping(Comment comment, @MappingTarget FullCommentResponse response) {
            if (comment.isDeleted( )) {
                response.setWriter(null);
            }
        }
    }
}
