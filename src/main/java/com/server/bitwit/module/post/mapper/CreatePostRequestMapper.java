package com.server.bitwit.module.post.mapper;

import com.server.bitwit.domain.Post;
import com.server.bitwit.domain.Tag;
import com.server.bitwit.infra.config.MapStructConfig;
import com.server.bitwit.module.account.mapper.AccountIdMapper;
import com.server.bitwit.module.post.dto.CreatePostRequest;
import com.server.bitwit.module.stock.StockRepository;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import com.server.bitwit.module.tag.TagRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(config = MapStructConfig.class, uses = AccountIdMapper.class, injectionStrategy = CONSTRUCTOR)
public abstract class CreatePostRequestMapper implements Converter<CreatePostRequest, Post> {
    
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
        var cond = new SearchStockCond( );
        cond.setTickers(tickers);
        stockRepository.searchStocks(cond)
                       .forEach(post::addStock);
    }
    
    private void addTags(List<String> tagNames, Post post) {
        tagNames.stream( )
                .map(name ->
                        tagRepository.findByName(name)
                                     .orElseGet(( ) -> tagRepository.save(new Tag(name)))
                )
                .forEach(post::addTag);
    }
}
