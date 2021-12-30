package com.server.bitwit.module.post.mapper;

import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.post.CommentService;
import com.server.bitwit.module.post.dto.PostResponse;
import com.server.bitwit.module.post.dto.PostViewer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public abstract class PostViewerToPostResponseMapper implements Converter<PostViewer, PostResponse> {
    
    @Lazy @Autowired
    private ConversionService conversionService;
    
    @Lazy @Autowired
    private CommentService commentService;
    
    @ObjectFactory
    public PostResponse mapToPostResponse(PostViewer postViewer) {
        return conversionService.convert(postViewer.getPost( ), PostResponse.class);
    }
    
    @AfterMapping
    public void setLike(PostViewer postViewer, @MappingTarget PostResponse postResponse) {
        postResponse.setLike(postViewer.getViewerAccountId( ));
        var comments       = postViewer.getPost( ).getComments( );
        var accountId      = postViewer.getViewerAccountId( );
        var nestedComments = commentService.convertToNestedComments(comments, accountId);
        postResponse.setComments(nestedComments);
    }
}
