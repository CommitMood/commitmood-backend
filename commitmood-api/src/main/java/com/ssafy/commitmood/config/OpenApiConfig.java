package com.ssafy.commitmood.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "CommitMood API",
                version = "v1",
                description = "GitHub 커밋 기반 감정 분석 서비스 API 문서",
                contact = @Contact(
                        name = "CommitMood Team",
                        email = "..."
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local"),
                @Server(url = "https://commitmood.web.app", description = "Prod")
        }
)
@Configuration
public class OpenApiConfig {
}