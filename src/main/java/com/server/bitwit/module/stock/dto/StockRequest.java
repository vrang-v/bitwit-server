package com.server.bitwit.module.stock.dto;

import com.server.bitwit.module.domain.Stock;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StockRequest
{
    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String name;
    
    public Stock toStock( )
    {
        return Stock.createStock(name);
    }
}
