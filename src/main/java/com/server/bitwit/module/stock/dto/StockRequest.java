package com.server.bitwit.module.stock.dto;

import com.server.bitwit.module.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest
{
    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String name;
    
    public Stock toStock( )
    {
        return Stock.createStock(name);
    }
}
