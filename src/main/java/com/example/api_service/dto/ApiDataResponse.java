package com.example.api_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API Data Response")
public class ApiDataResponse {
    @Schema(description = "Unique identifier")
    private UUID id;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Success status")
    private boolean success;
    
    @Schema(description = "API response payload")
    private String payload;
    
    // Конструктор для создания объекта только с payload
    public ApiDataResponse(String payload) {
        this.payload = payload;
    }
}
