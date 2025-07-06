package com.example.api_service.mapper;

import com.example.api_service.dto.ApiDataRequest;
import com.example.api_service.dto.ApiDataResponse;
import com.example.api_service.model.ApiDataEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApiDataMapperTest {

    private final ApiDataMapper mapper = Mappers.getMapper(ApiDataMapper.class);
    private String payload1;
    private String payload2;
    private UUID id;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        payload1 = "{\"USD\":50000,\"EUR\":45000}";
        payload2 = "{\"USD\":55000,\"EUR\":50000}";
        createdAt = LocalDateTime.now();
    }

    @Test
    void toApiDataEntityTest() {
        // Given
        ApiDataRequest request = new ApiDataRequest(payload1);

        // When
        ApiDataEntity entity = mapper.toApiDataEntity(request);

        // Then
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getCreatedAt());
        assertEquals(payload1, entity.getPayload());
    }

    @Test
    void toApiDataResponseTest() {
        // Given
        ApiDataEntity entity = new ApiDataEntity();
        entity.setId(id);
        entity.setCreatedAt(createdAt);
        entity.setSuccess(true);
        entity.setPayload(payload1);

        // When
        ApiDataResponse response = mapper.toApiDataResponse(entity);

        // Then
        assertNotNull(response);
        assertEquals(payload1, response.getPayload());
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    void toApiDataResponseListTest() {
        // Given
        ApiDataEntity entity1 = new ApiDataEntity();
        entity1.setPayload(payload1);
        entity1.setCreatedAt(createdAt);

        ApiDataEntity entity2 = new ApiDataEntity();
        entity2.setPayload(payload2);
        entity2.setCreatedAt(createdAt);

        List<ApiDataEntity> entities = Arrays.asList(entity1, entity2);

        // When
        List<ApiDataResponse> responses = mapper.toApiDataResponseList(entities);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(payload1, responses.get(0).getPayload());
        assertEquals(payload2, responses.get(1).getPayload());
    }

    @Test
    void toApiDataResponseWithEmptyList() {
        // When
        List<ApiDataResponse> responses = mapper.toApiDataResponseList(Collections.emptyList());

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
} 