package com.example.api_service.controller;

import com.example.api_service.dto.ApiDataResponse;
import com.example.api_service.service.ApiDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "API Monitoring Service", description = "API для мониторинга сервиса")
public class ApiDataController {
    private final ApiDataService service;

    @GetMapping("/status")
    @Operation(summary = "Проверка статуса сервиса", description = "Проверяет работоспособность сервиса.")
    public ResponseEntity<Boolean> getStatus() {
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("/data")
    @Operation(summary = "Получение последних 10 записей", description = "Возвращает 10 последних записей.")
    public ResponseEntity<List<ApiDataResponse>> getLast10Records() {
        return ResponseEntity.ok(service.findTop10RecordsAsResponse());
    }
}