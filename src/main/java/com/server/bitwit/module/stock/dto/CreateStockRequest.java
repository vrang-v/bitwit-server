package com.server.bitwit.module.stock.dto;

import com.server.bitwit.domain.Stock;
import com.server.bitwit.infra.config.MapStructConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStockRequest {
    
    @NotBlank(message = "티커는 비어있을 수 없습니다.")
    private String ticker;
    
    @NotBlank(message = "영어명은 비어있을 수 없습니다.")
    private String fullName;
    
    @NotBlank(message = "한글명은 비어있을 수 없습니다.")
    private String koreanName;
    
    
    @Mapper(config = MapStructConfig.class)
    public interface CreateStockRequestMapper extends Converter<CreateStockRequest, Stock> {
        
        @ObjectFactory
        default Stock createStock(CreateStockRequest request) {
            return Stock.createStock(request.getTicker( ), request.getFullName( ), request.getKoreanName( ));
        }
    }
}
