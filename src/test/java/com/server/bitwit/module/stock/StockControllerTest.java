package com.server.bitwit.module.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.bitwit.module.error.exception.ErrorCode;
import com.server.bitwit.module.stock.dto.CreateStockRequest;
import com.server.bitwit.module.stock.dto.StockResponse;
import com.server.bitwit.util.MockMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        
        // then
        mockMvc.perform(post("/api/stocks")
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(status( ).isCreated( ));
    }
    
    @Test
    @DisplayName("Stock 생성 / 요청 값 없음 / 400")
    void createStock_noRequestContent_400( ) throws Exception {
        mockMvc.perform(post("/api/stocks"))
               .andExpect(status( ).isBadRequest( ));
    }
    
    @Test
    @DisplayName("Stock 생성 / 유효하지 않은 요청 / 400")
    void createStock_invalidRequest_400( ) throws Exception {
        // given
        var request = new CreateStockRequest( );
        request.setFullName(null);
        
        // then
        mockMvc.perform(post("/api/stocks")
                       .contentType(APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpect(status( ).isBadRequest( ));
    }
    
    @Test
    @DisplayName("Stock 조회 / 정상 / 200")
    void getStock( ) throws Exception {
        // given
        var stockId = createMockStock( ).getId( );
        
        // then
        mockMvc.perform(get("/api/stocks/{stockId}", stockId)
               )
               .andExpectAll(
                       status( ).isOk( ),
                       jsonPath("$.stockId").value(stockId),
                       jsonPath("$.fullName").value(STOCK_NAME)
               );
    }
    
    @Test
    @DisplayName("Stock 조회 / 존재하지 않는 id / 404")
    void getStock_nonExistentId_404( ) throws Exception {
        var nonExistentId = - 1;
        mockMvc.perform(get("/api/stocks/{stockId}", nonExistentId)
               )
               .andExpectAll(
                       status( ).isNotFound( ),
                       jsonPath("$.code").value(ErrorCode.RESOURCE_NOT_FOUND.getCode( ))
               );
    }
    
    private StockResponse createMockStock( ) {
        var request = new CreateStockRequest( );
        request.setFullName(StockControllerTest.STOCK_NAME);
        
        return stockService.createStock(request);
    }
}
