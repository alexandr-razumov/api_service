package com.example.api_service.mapper;

import com.example.api_service.dto.ApiDataResponse;
import com.example.api_service.model.ApiDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiDataMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "success", ignore = true)
    ApiDataEntity toApiDataEntity(ApiDataResponse response);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "success", target = "success")
    @Mapping(source = "payload", target = "payload")
    ApiDataResponse toApiDataResponse(ApiDataEntity entity);
}
