package com.example.api_service.repository;

import com.example.api_service.model.ApiDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApiDataRepository extends JpaRepository<ApiDataEntity, UUID> {
    List<ApiDataEntity> findTop10ByOrderByCreatedAtDesc();
}
