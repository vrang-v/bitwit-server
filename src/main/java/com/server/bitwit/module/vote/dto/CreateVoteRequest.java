package com.server.bitwit.module.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoteRequest {
    
    @NotNull
    Long stockId;
    
    @NotBlank
    String description;
    
    @NotNull
    LocalDateTime startAt;
    
    @NotNull
    LocalDateTime endedAt;
}
