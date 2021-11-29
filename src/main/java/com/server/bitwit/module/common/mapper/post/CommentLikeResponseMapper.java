package com.server.bitwit.module.common.mapper.post;

import com.server.bitwit.domain.CommentLike;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.post.dto.LikeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class)
public interface CommentLikeResponseMapper extends Converter<CommentLike, LikeResponse> {
    
    @Override
    @Mapping(target = "accountId", expression = "java(like.getAccount().getId())")
    LikeResponse convert(CommentLike like);
    
}
