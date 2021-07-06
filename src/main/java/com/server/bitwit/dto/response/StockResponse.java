package com.server.bitwit.dto.response;

import com.server.bitwit.domain.Stock;
import lombok.Data;

@Data
public class StockResponse
{
    private Long id;
    
    private String name;
    
    public static StockResponse fromStock(Stock stock)
    {
        var response = new StockResponse( );
        response.id   = stock.getId( );
        response.name = stock.getName( );
        return response;
    }
}
