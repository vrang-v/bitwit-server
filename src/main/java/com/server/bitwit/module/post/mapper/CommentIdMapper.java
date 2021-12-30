package com.server.bitwit.module.post.mapper;

import com.server.bitwit.domain.Comment;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.post.CommentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public abstract class CommentIdMapper implements Converter<Long, Comment> {
    
    @Lazy @Autowired CommentRepository commentRe;
    
    @ObjectFactory
    public Comment getPost(Long commentId) {
        return commentRe.findById(commentId)
                        .orElseThrow(( ) -> new NonExistentResourceException("comment", commentId));
    }
    
}
