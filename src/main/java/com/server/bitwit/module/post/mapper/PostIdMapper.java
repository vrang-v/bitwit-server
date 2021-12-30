package com.server.bitwit.module.post.mapper;

import com.server.bitwit.domain.Post;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.error.exception.NonExistentResourceException;
import com.server.bitwit.module.post.PostRepository;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public abstract class PostIdMapper implements Converter<Long, Post> {
    
    @Lazy @Autowired PostRepository postRepository;
    
    @ObjectFactory
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                             .orElseThrow(( ) -> new NonExistentResourceException("post", postId));
    }
}
