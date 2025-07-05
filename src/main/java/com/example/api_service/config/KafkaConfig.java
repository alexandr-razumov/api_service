package com.example.api_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic apiDataTopic() {
        return new NewTopic("api-data", 1, (short) 1);
    }

    @Bean
    public NewTopic apiErrorsTopic() {
        return new NewTopic("api-errors", 1, (short) 1);
    }
}
