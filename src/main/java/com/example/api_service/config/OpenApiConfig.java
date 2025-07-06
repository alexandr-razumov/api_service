package com.example.api_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("API Monitoring Service")
                        .version("v1")
                        .description("API для получения данных с заданного адреса. " +
                                   "Требует HTTP Basic Authentication. " +
                                   "Доступные роли: USER (статус), ADMIN (статус + данные)"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("HTTP Basic Authentication. " +
                                            "Используйте логин и пароль для входа:\n" +
                                            "- user/user (роль USER)\n" +
                                            "- admin/admin (роль ADMIN)")));
    }
}