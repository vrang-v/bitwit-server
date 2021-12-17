package com.server.bitwit.module.common.mapper.post;

import com.server.bitwit.domain.Post;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.common.mapper.account.AccountResponseMapper;
import com.server.bitwit.module.common.mapper.stock.StockResponseMapper;
import com.server.bitwit.module.post.dto.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapStructConfig.class, uses = {AccountResponseMapper.class, StockResponseMapper.class, PostLikeResponseMapper.class})
public interface PostResponseMapper extends Converter<Post, PostResponse> {
    
    @Override
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "likeCount", expression = "java(post.getLikes().size())")
    @Mapping(target = "commentCount", expression = "java(post.getComments().size())")
    PostResponse convert(Post post);
    
}
