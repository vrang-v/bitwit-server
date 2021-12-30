package com.server.bitwit.module.post.mapper;

import com.server.bitwit.domain.Comment;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.mapper.AccountResponseMapper;
import com.server.bitwit.module.post.dto.CommentResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

@Mapper(config = MapStructConfig.class, uses = {AccountResponseMapper.class, CommentLikeResponseMapper.class})
public interface CommentResponseMapper extends Converter<Comment, CommentResponse> {
    
    @Override
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parentId", source = "comment")
    @Mapping(target = "likeCount", expression = "java(comment.getLikes().size())")
    CommentResponse convert(Comment comment);
    
    default Long parentId(Comment comment) {
        return Optional.ofNullable(comment.getParent( ))
                       .map(Comment::getId)
                       .orElse(null);
    }
    
    @AfterMapping
    default void afterMapping(Comment comment, @MappingTarget CommentResponse response) {
        if (comment.isDeleted( )) {
            response.setWriter(null);
        }
    }
}
