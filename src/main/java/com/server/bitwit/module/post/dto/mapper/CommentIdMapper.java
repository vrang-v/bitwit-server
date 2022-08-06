package com.server.bitwit.module.post.dto.mapper;

import com.server.bitwit.domain.Comment;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.post.repository.CommentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public abstract class CommentIdMapper implements Converter<Long, Comment> {
    
    @Lazy @Autowired CommentRepository commentRepository;
    
    @ObjectFactory
    public Comment getPost(Long commentId) {
        if (commentId == null) {
            return null;
        }
        return commentRepository.findById(commentId)
                                .orElseThrow(( ) -> new NonExistentResourceException("comment", commentId));
    }
    
}
