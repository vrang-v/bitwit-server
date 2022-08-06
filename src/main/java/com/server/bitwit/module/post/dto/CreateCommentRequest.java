package com.server.bitwit.module.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.server.bitwit.domain.Comment;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.mapper.AccountIdMapper;
import com.server.bitwit.module.error.exception.BitwitException;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.post.dto.mapper.CommentIdMapper;
import com.server.bitwit.module.post.dto.mapper.PostIdMapper;
import com.server.bitwit.module.post.repository.CommentRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CreateCommentRequest {
    
    @Nullable
    Long parentId;
    
    @JsonIgnore
    @Null(message = "AccountId는 임의로 지정할 수 없습니다. JWT 토큰에서 가져옵니다.")
    Long accountId;
    
    @NotNull
    Long postId;
    
    @NotBlank
    String content;
    
    public CreateCommentRequest withAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }
    
    
    @Mapper(config = MapStructConfig.class, uses = {AccountIdMapper.class, PostIdMapper.class, CommentIdMapper.class})
    public abstract static class CreateCommentRequestMapper implements Converter<CreateCommentRequest, Comment> {
        
        @Lazy @Autowired CommentRepository commentRepository;
        
        @Override
        @Mapping(target = "parent", source = "parentId")
        @Mapping(target = "writer", source = "accountId")
        @Mapping(target = "post", source = "postId")
        public abstract Comment convert(CreateCommentRequest source);
        
        @BeforeMapping
        void beforeMapping(CreateCommentRequest request) {
            if (request.getParentId( ) != null
                && ! commentRepository.existParent(request.getPostId( ), request.getParentId( ))) {
                throw new BitwitException(ErrorCode.INVALID_REQUEST);
            }
        }
    }
}
