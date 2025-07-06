package com.example.api_service.service;

import com.example.api_service.config.KafkaConfig;
import com.example.api_service.dto.ApiDataRequest;
import com.example.api_service.dto.ApiDataResponse;
import com.example.api_service.repository.ApiDataRepository;
import com.example.api_service.mapper.ApiDataMapper;
import com.example.api_service.model.ApiDataEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiDataService {
    private final RestTemplate restTemplate;
    private final ApiDataRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;
    private final ApiDataMapper apiDataMapper;

    @Value("${api.url}")
    private String apiUrl;

    @Scheduled(fixedRate = 60000)
    @Retryable(backoff = @Backoff(delay = 1000, multiplier = 2.0))
    public void pollApi() {
        log.info("Starting API poll for URL: {}", apiUrl);
        ApiDataRequest request;
        boolean success;
        String kafkaTopic;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            request = new ApiDataRequest(response.getBody());
            success = true;
            kafkaTopic = kafkaConfig.apiDataTopic().name();
            log.info("API call successful, status: {}", response.getStatusCode());
        } catch (Exception e) {
            request = new ApiDataRequest(e.getMessage());
            success = false;
            kafkaTopic = kafkaConfig.apiErrorsTopic().name();
            log.error("API call failed: {}", e.getMessage(), e);
        }

        ApiDataEntity entity = apiDataMapper.toApiDataEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setSuccess(success);
        repository.save(entity);
        kafkaTemplate.send(kafkaTopic, UUID.randomUUID().toString(), request.getPayload());
        log.info("Data saved to database and sent to Kafka topic: {}", kafkaTopic);

        if (!success) {
            throw new RuntimeException(request.getPayload());
        }
    }

    public List<ApiDataResponse> findTop10RecordsAsResponse() {
        return apiDataMapper.toApiDataResponseList(repository.findTop10ByOrderByCreatedAtDesc());
    }
}