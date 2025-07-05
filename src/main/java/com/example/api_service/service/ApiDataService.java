package com.example.api_service.service;

import com.example.api_service.config.KafkaConfig;
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
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2.0))
    public void pollApi() {
        log.info("Starting API poll for URL: {}", apiUrl);
        ApiDataResponse apiDataResponse;
        boolean success;
        String kafkaTopic;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            apiDataResponse = new ApiDataResponse(response.getBody());
            success = true;
            kafkaTopic = kafkaConfig.apiDataTopic().name();
            log.info("API call successful, status: {}", response.getStatusCode());
        } catch (Exception e) {
            apiDataResponse = new ApiDataResponse(e.getMessage());
            success = false;
            kafkaTopic = kafkaConfig.apiErrorsTopic().name();
            log.error("API call failed: {}", e.getMessage(), e);
        }

        ApiDataEntity entity = apiDataMapper.toApiDataEntity(apiDataResponse);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setSuccess(success);
        repository.save(entity);
        kafkaTemplate.send(kafkaTopic, apiDataResponse.getPayload());
        log.info("Data saved to database and sent to Kafka topic: {}", kafkaTopic);

        if (!success) {
            throw new RuntimeException(apiDataResponse.getPayload());
        }
    }

    public List<ApiDataEntity> findTop10ByOrderByCreatedAtDesc() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<ApiDataResponse> findTop10RecordsAsResponse() {
        List<ApiDataResponse> responses = repository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(apiDataMapper::toApiDataResponse)
                .toList();
        log.debug("Retrieved {} records from database", responses.size());
        return responses;
    }
}