package com.server.bitwit.module.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.stock.dto.CreateStockRequest;
import com.server.bitwit.module.stock.dto.StockResponse;
import com.server.bitwit.util.MockMvcTest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
@Transactional
class StockControllerTest {
    
    static final String STOCK_NAME        = "Apple.inc";
    static final String STOCK_TICKER      = "AAPL";
    static final String STOCK_KOREAN_NAME = "애플";
    
    @Autowired MockMvc      mockMvc;
    @Autowired ObjectMapper objectMapper;
    
    @Autowired StockService stockService;
    
    @Test
    @DisplayName("Stock 생성 / 정상 / 201")
    void createStock_normal_200( ) throws Exception {
        // given
        var request = new CreateStockRequest( );
        request.setFullName(STOCK_NAME);
        request.setTicker(STOCK_TICKER);
        request.setKoreanName(STOCK_KOREAN_NAME);
        
        // when, then
        mockMvc.perform(post("/api/stocks")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(APPLICATION_JSON)
                       .accept(APPLICATION_JSON_UTF8)
               )
               .andExpect(status( ).isCreated( ));
    }
    
    @Test
    @DisplayName("Stock 생성 / 요청 값 없음 / 400")
    void createStock_noRequestContent_400( ) throws Exception {
        // when, then
        mockMvc.perform(post("/api/stocks")
                       .accept(APPLICATION_JSON_UTF8))
               .andExpect(status( ).isBadRequest( ));
    }
    
    @Test
    @DisplayName("Stock 생성 / 유효하지 않은 요청 / 400")
    void createStock_invalidRequest_400( ) throws Exception {
        // given
        var request = new CreateStockRequest( );
        request.setFullName(null);
        
        // when, then
        mockMvc.perform(post("/api/stocks")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(APPLICATION_JSON)
               )
               .andExpect(status( ).isBadRequest( ));
    }
    
    @Test
    @DisplayName("Stock 조회 / 정상 / 200")
    void getStock( ) throws Exception {
        // given
        var stockId = createMockStock(STOCK_NAME).getId( );
        
        // when, then
        mockMvc.perform(get("/api/stocks/" + stockId)
                       .accept(APPLICATION_JSON_UTF8)
               )
               .andExpect(status( ).isOk( ))
               .andExpect(jsonPath("$.stockId").value(stockId))
               .andExpect(jsonPath("$.fullName").value(STOCK_NAME));
    }
    
    @Test
    @DisplayName("Stock 조회 / 존재하지 않는 id / 404")
    void getStock_nonExistentId_400( ) throws Exception {
        var nonExistentId = - 1;
        mockMvc.perform(get("/api/stocks/" + nonExistentId))
               .andExpect(status( ).isNotFound( ));
    }
    
    private StockResponse createMockStock(String name) {
        var request = new CreateStockRequest( );
        request.setFullName(name);
        
        return stockService.createStock(request);
    }
}
