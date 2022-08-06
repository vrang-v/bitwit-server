package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.bitwit.domain.CommentLike;
import com.server.bitwit.domain.PostLike;
import com.server.bitwit.infra.config.MapStructConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {
    
    @JsonProperty("likeId")
    Long id;
    
    Long accountId;
    
    boolean like;
    
    public LikeResponse setLike( ) {
        this.like = true;
        return this;
    }
    
    public LikeResponse setDislike( ) {
        this.like = false;
        return this;
    }
    
    
    @Mapper(config = MapStructConfig.class)
    public interface PostLikeResponseMapper extends Converter<PostLike, LikeResponse> {
        
        @Override
        @Mapping(target = "accountId", expression = "java(like.getAccount().getId())")
        LikeResponse convert(PostLike like);
        
    }
    
    
    @Mapper(config = MapStructConfig.class)
    public interface CommentLikeResponseMapper extends Converter<CommentLike, LikeResponse> {
        
        @Override
        @Mapping(target = "accountId", expression = "java(like.getAccount().getId())")
        LikeResponse convert(CommentLike like);
        
    }
    
}
