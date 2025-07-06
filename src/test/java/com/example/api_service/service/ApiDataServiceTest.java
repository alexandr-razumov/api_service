package com.example.api_service.service;

import com.example.api_service.config.KafkaConfig;
import com.example.api_service.dto.ApiDataRequest;
import com.example.api_service.dto.ApiDataResponse;
import com.example.api_service.mapper.ApiDataMapper;
import com.example.api_service.model.ApiDataEntity;
import com.example.api_service.repository.ApiDataRepository;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiDataServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApiDataRepository repository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private KafkaConfig kafkaConfig;

    @Mock
    private ApiDataMapper apiDataMapper;

    @Mock
    private NewTopic apiDataTopic;

    @Mock
    private NewTopic apiErrorsTopic;

    @InjectMocks
    private ApiDataService apiDataService;

    private final String testApiUrl = "https://test-api.com";
    private final String testPayload = "{\"USD\":50000,\"EUR\":45000}";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(apiDataService, "apiUrl", testApiUrl);
        lenient().when(kafkaConfig.apiDataTopic()).thenReturn(apiDataTopic);
        lenient().when(kafkaConfig.apiErrorsTopic()).thenReturn(apiErrorsTopic);
        lenient().when(apiDataTopic.name()).thenReturn("api-data-topic");
        lenient().when(apiErrorsTopic.name()).thenReturn("api-errors-topic");
    }

    @Test
    void pollApiSuccess() {
        // Given
        ResponseEntity<String> responseEntity = new ResponseEntity<>(testPayload, HttpStatus.OK);
        ApiDataEntity expectedEntity = createTestEntity(UUID.randomUUID(), LocalDateTime.now(), true, testPayload);

        when(restTemplate.getForEntity(testApiUrl, String.class)).thenReturn(responseEntity);
        when(apiDataMapper.toApiDataEntity(any(ApiDataRequest.class))).thenReturn(expectedEntity);
        when(repository.save(any(ApiDataEntity.class))).thenReturn(expectedEntity);
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(null);

        // When
        apiDataService.pollApi();

        // Then
        verify(restTemplate).getForEntity(testApiUrl, String.class);
        verify(apiDataMapper).toApiDataEntity(any(ApiDataRequest.class));
        verify(repository).save(any(ApiDataEntity.class));
        verify(kafkaTemplate).send(eq("api-data-topic"), anyString(), eq(testPayload));
    }

    @Test
    void pollApiFailure() {
        // Given
        String testErrorPayload = "Connection failed";
        RestClientException apiException = new RestClientException(testErrorPayload);
        ApiDataEntity expectedEntity = createTestEntity(UUID.randomUUID(), LocalDateTime.now(), false, testErrorPayload);

        when(restTemplate.getForEntity(testApiUrl, String.class)).thenThrow(apiException);
        when(apiDataMapper.toApiDataEntity(any(ApiDataRequest.class))).thenReturn(expectedEntity);
        when(repository.save(any(ApiDataEntity.class))).thenReturn(expectedEntity);
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> apiDataService.pollApi());

        verify(restTemplate).getForEntity(testApiUrl, String.class);
        verify(apiDataMapper).toApiDataEntity(any(ApiDataRequest.class));
        verify(repository).save(any(ApiDataEntity.class));
        verify(kafkaTemplate).send(eq("api-errors-topic"), anyString(), eq(testErrorPayload));
    }

    @Test
    void findTop10RecordsAsResponse() {
        // Given
        List<ApiDataEntity> entities = Arrays.asList(
            createTestEntity(UUID.randomUUID(), LocalDateTime.now(), true, "test payload 1"),
            createTestEntity(UUID.randomUUID(), LocalDateTime.now(), false, "test payload 2")
        );
        
        List<ApiDataResponse> expectedResponses = Arrays.asList(
            new ApiDataResponse("test payload 1", LocalDateTime.now()),
            new ApiDataResponse("test payload 2", LocalDateTime.now())
        );

        when(repository.findTop10ByOrderByCreatedAtDesc()).thenReturn(entities);
        when(apiDataMapper.toApiDataResponseList(entities)).thenReturn(expectedResponses);

        // When
        List<ApiDataResponse> result = apiDataService.findTop10RecordsAsResponse();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResponses, result);
        
        verify(repository).findTop10ByOrderByCreatedAtDesc();
        verify(apiDataMapper).toApiDataResponseList(entities);
    }

    @Test
    void pollApiGenericException() {
        // Given
        RuntimeException genericException = new RuntimeException("Generic error");
        ApiDataEntity expectedEntity = createTestEntity(UUID.randomUUID(), LocalDateTime.now(), false, "Generic error");

        when(restTemplate.getForEntity(testApiUrl, String.class)).thenThrow(genericException);
        when(apiDataMapper.toApiDataEntity(any(ApiDataRequest.class))).thenReturn(expectedEntity);
        when(repository.save(any(ApiDataEntity.class))).thenReturn(expectedEntity);
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> apiDataService.pollApi());

        verify(restTemplate).getForEntity(testApiUrl, String.class);
        verify(apiDataMapper).toApiDataEntity(any(ApiDataRequest.class));
        verify(repository).save(any(ApiDataEntity.class));
        verify(kafkaTemplate).send(eq("api-errors-topic"), anyString(), eq("Generic error"));
    }

    private ApiDataEntity createTestEntity(UUID id, LocalDateTime createdAt, boolean success, String payload) {
        ApiDataEntity entity = new ApiDataEntity();
        entity.setId(id);
        entity.setCreatedAt(createdAt);
        entity.setSuccess(success);
        entity.setPayload(payload);
        return entity;
    }
}

