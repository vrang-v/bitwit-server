package com.server.bitwit.module.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
}
