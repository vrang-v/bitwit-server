package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.Comment;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.dto.AccountResponse;
import com.server.bitwit.module.account.dto.AccountResponse.AccountResponseMapper;
import com.server.bitwit.module.post.dto.PostResponse.PostResponseMapper;
import lombok.Data;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class CommentWithPostResponse {
    
    @JsonProperty("commentId")
    Long id;
    
    String content;
    
    AccountResponse writer;
    
    int depth;
    
    boolean edited;
    
    boolean deleted;
    
    int likeCount;
    
    boolean like;
    
    Instant createdAt;
    
    Long parentId;
    
    List<CommentResponse> children = new ArrayList<>( );
    
    PostResponse post;
    
    @JsonIgnore
    Set<LikeResponse> likes;
    
    public CommentWithPostResponse setLike(Long accountId) {
        like = likes.stream( )
                    .map(LikeResponse::getAccountId)
                    .anyMatch(accountId::equals);
        return this;
    }
    
    
    @Mapper(config = MapStructConfig.class, uses = {AccountResponseMapper.class, PostResponseMapper.class})
    public interface CommentWithPostResponseMapper extends Converter<Comment, CommentWithPostResponse> {
        
        @Override
        @Mapping(target = "children", ignore = true)
        @Mapping(target = "parentId", source = "parent.id")
        @Mapping(target = "likeCount", expression = "java(comment.getLikes().size())")
        CommentWithPostResponse convert(Comment comment);
        
        @AfterMapping
        default void afterMapping(Comment comment, @MappingTarget CommentWithPostResponse response) {
            if (comment.isDeleted( )) {
                response.setWriter(null);
            }
        }
    }
}
