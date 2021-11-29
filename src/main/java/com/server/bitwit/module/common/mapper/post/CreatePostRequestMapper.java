package com.server.bitwit.module.common.mapper.post;

import com.server.bitwit.domain.Post;
import com.server.bitwit.module.common.mapper.MapStructConfig;
import com.server.bitwit.module.common.mapper.account.AccountIdMapper;
import com.server.bitwit.module.post.dto.CreatePostRequest;
import com.server.bitwit.module.stock.StockRepository;
import com.server.bitwit.module.stock.dto.SearchStockCond;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(config = MapStructConfig.class, uses = AccountIdMapper.class, injectionStrategy = CONSTRUCTOR)
public abstract class CreatePostRequestMapper implements Converter<CreatePostRequest, Post> {
    
    @Lazy @Autowired
    private StockRepository stockRepository;
    
    @Override
    @Mapping(target = "writer", source = "accountId")
    public abstract Post convert(CreatePostRequest request);
    
    @AfterMapping
    public void addStocks(CreatePostRequest request, @MappingTarget Post post) {
        var cond = new SearchStockCond( );
        cond.setTickers(request.getTickers( ));
        stockRepository.searchStocks(cond)
                       .forEach(post::addStock);
    }
}
