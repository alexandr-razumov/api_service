package com.example.api_service.controller;

import com.example.api_service.dto.ApiDataResponse;
import com.example.api_service.exception_handler.ResourceNotFoundException;
import com.example.api_service.service.ApiDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "API Monitoring Service", description = "API для мониторинга сервиса")
@SecurityRequirement(name = "basicAuth")
public class ApiDataController {
    private final ApiDataService service;

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Проверка статуса сервиса", description = "Проверяет работоспособность сервиса. Доступно для USER и ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Сервис работает"),
        @ApiResponse(responseCode = "401", description = "Не авторизован", 
                    content = @Content(schema = @Schema(implementation = com.example.api_service.dto.ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен", 
                    content = @Content(schema = @Schema(implementation = com.example.api_service.dto.ErrorResponse.class)))
    })
    public ResponseEntity<Boolean> getStatus() {
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/data")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение последних записей", description = "Возвращает последние записи. Доступно только для ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Данные получены успешно"),
        @ApiResponse(responseCode = "400", description = "Некорректный запрос", 
                    content = @Content(schema = @Schema(implementation = com.example.api_service.dto.ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Не авторизован", 
                    content = @Content(schema = @Schema(implementation = com.example.api_service.dto.ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Доступ запрещен", 
                    content = @Content(schema = @Schema(implementation = com.example.api_service.dto.ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Данные не найдены", 
                    content = @Content(schema = @Schema(implementation = com.example.api_service.dto.ErrorResponse.class)))
    })
    public ResponseEntity<List<ApiDataResponse>> getLastRecords() {
        List<ApiDataResponse> records = service.findTop10RecordsAsResponse();
        if (records.isEmpty()) {
            throw new ResourceNotFoundException("No data records found");
        }
        return ResponseEntity.ok(records);
    }
}