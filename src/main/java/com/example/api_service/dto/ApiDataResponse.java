package com.example.api_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Schema(description = "API Response",
            example = "{\"USD\":50000,\"EUR\":45000}")
    private String payload;

    @Schema(description = "Created Date")
    private LocalDateTime createdAt;
}
