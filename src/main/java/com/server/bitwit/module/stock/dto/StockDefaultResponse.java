package com.server.bitwit.module.stock.dto;

import com.server.bitwit.module.domain.Stock;
import lombok.Data;

@Data
public class StockDefaultResponse implements StockResponse
{
    private Long id;
    
    private String name;
    
    public static StockDefaultResponse fromStock(Stock stock)
    {
        var response = new StockDefaultResponse( );
        response.id   = stock.getId( );
        response.name = stock.getName( );
        return response;
    }
}
