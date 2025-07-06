package com.example.api_service.mapper;

import com.example.api_service.dto.ApiDataRequest;
import com.example.api_service.dto.ApiDataResponse;
import com.example.api_service.model.ApiDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApiDataMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "success", ignore = true)
    ApiDataEntity toApiDataEntity(ApiDataRequest request);

    ApiDataResponse toApiDataResponse(ApiDataEntity entity);
    List<ApiDataResponse> toApiDataResponseList(List<ApiDataEntity> entities);
}
