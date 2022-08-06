package com.server.bitwit.module.post.dto;

import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.Tag;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.mapper.AccountIdMapper;
import com.server.bitwit.module.stock.StockRepository;
import com.server.bitwit.module.stock.search.SearchStockCond;
import com.server.bitwit.module.tag.TagRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Data
@NoArgsConstructor
public class CreatePostRequest {
    
    @NotBlank
    String title;
    
    @NotBlank
    String content;
    
    @Null(message = "AccountId는 임의로 지정할 수 없습니다. JWT 토큰에서 가져옵니다.")
    Long accountId;
    
    List<String> tickers = new ArrayList<>( );
    
    List<String> tags;
    
    public CreatePostRequest withAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }
    
    public void distinctTickers( ) {
        tickers = tickers.stream( ).distinct( ).toList( );
    }
    
    
    @Mapper(config = MapStructConfig.class, uses = AccountIdMapper.class, injectionStrategy = CONSTRUCTOR)
    public abstract static class CreatePostRequestMapper implements Converter<CreatePostRequest, Post> {
        
        @Lazy @Autowired
        private StockRepository stockRepository;
        
        @Lazy @Autowired
        private TagRepository tagRepository;
        
        @Override
        @Mapping(target = "tags", ignore = true)
        @Mapping(target = "writer", source = "accountId")
        public abstract Post convert(CreatePostRequest request);
        
        @AfterMapping
        public void afterConverting(CreatePostRequest request, @MappingTarget Post post) {
            addStocks(request.getTickers( ), post);
            addTags(request.getTags( ), post);
        }
        
        private void addStocks(List<String> tickers, Post post) {
            if (tickers == null) {
                return;
            }
            var cond = new SearchStockCond( );
            cond.setTickers(tickers);
            stockRepository.searchStocks(cond)
                           .forEach(post::addStock);
        }
        
        private void addTags(List<String> tagNames, Post post) {
            if (tagNames == null) {
                return;
            }
            tagNames.stream( )
                    .map(name ->
                            tagRepository.findByName(name)
                                         .orElseGet(( ) -> tagRepository.save(new Tag(name)))
                    )
                    .forEach(post::addTag);
        }
    }
}
