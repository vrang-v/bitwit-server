package com.server.bitwit.modle.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.stock.StockService;
import com.server.bitwit.module.stock.dto.StockRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StockControllerTest
{
    static final String STOCK_NAME = "AAPL";
    
    @Autowired MockMvc      mockMvc;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired StockService stockService;
    
    @Test
    @DisplayName("Stock 생성 / 정상 / 201")
    void createStock_normal_200( ) throws Exception
    {
        // given
        var request = new StockRequest( );
        request.setName(STOCK_NAME);
        
        // when, then
        mockMvc.perform(post("/stocks")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(APPLICATION_JSON)
                       .accept(APPLICATION_JSON))
               .andExpect(status( ).isCreated( ));
    }
    
    @Test
    @DisplayName("Stock 생성 / 요청 값 없음 / 400")
    void createStock_noRequestContent_400( ) throws Exception
    {
        // when, then
        mockMvc.perform(post("/stocks"))
               .andExpect(status( ).isBadRequest( ));
    }
    
    @Test
    @DisplayName("Stock 생성 / 유효하지 않은 요청 / 400")
    void createStock_invalidRequest_400( ) throws Exception
    {
        // given
        var request = new StockRequest( );
        request.setName(null);
        
        // when, then
        mockMvc.perform(post("/stocks")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(APPLICATION_JSON))
               .andExpect(status( ).isBadRequest( ));
    }
    
    @Test
    @DisplayName("Stock 조회 / 정상 / 200")
    void getStock( ) throws Exception
    {
        // given
        var stockId = createMockStock(STOCK_NAME);
        var expectedJson = new JSONObject( )
                .put("id", stockId)
                .put("name", STOCK_NAME)
                .toString( );
        
        // when, then
        mockMvc.perform(get("/stocks/" + stockId))
               .andExpect(status( ).isOk( ))
               .andExpect(content( ).json(expectedJson));
    }
    
    @Test
    @DisplayName("Stock 조회 / 존재하지 않는 id / 404")
    void getStock_nonExistentId_400( ) throws Exception
    {
        var nonExistentId = - 1;
        mockMvc.perform(get("/stocks/" + nonExistentId))
               .andExpect(status( ).isNotFound( ));
    }
    
    private Long createMockStock(String name)
    {
        var request = new StockRequest( );
        request.setName(name);
        
        return stockService.createStock(request);
    }
}
