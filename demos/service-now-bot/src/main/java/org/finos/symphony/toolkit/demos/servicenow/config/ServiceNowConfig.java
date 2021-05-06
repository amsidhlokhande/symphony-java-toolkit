package org.finos.symphony.toolkit.demos.servicenow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ServiceNowConfig {

    @Value("${service.now.base.url: baseUrl}")
    public String serviceNowBaseUrl;

    @Value("${service.now.base.url: baseUrl}")
    public String serviceNowUserName;

    @Value("${service.now.base.url: baseUrl}")
    public String serviceNowPassword;

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder().baseUrl(serviceNowBaseUrl).defaultHeaders(httpHeaders -> {
            httpHeaders.setBasicAuth(serviceNowUserName, serviceNowPassword);
            httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        });
    }
}
