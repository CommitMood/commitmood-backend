package com.ssafy.commitmood.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("github.api")
@Getter
@Setter
public class GithubApiProperties {
    private String baseUrl;
}
