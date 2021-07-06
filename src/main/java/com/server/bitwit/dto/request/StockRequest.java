package com.server.bitwit.dto.request;

import com.server.bitwit.domain.Stock;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StockRequest
{
    @NotBlank
    private String name;
    
    public Stock toStock( )
    {
        return Stock.createStock(name);
    }
}
