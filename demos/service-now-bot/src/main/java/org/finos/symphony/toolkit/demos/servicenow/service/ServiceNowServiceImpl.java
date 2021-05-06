package org.finos.symphony.toolkit.demos.servicenow.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Data
@Slf4j
@Service
public class ServiceNowServiceImpl implements ServiceNowService {

    @Value("${service.now.base.url: baseUrl}")
    public String serviceNowRITMUrl;

    private final WebClient.Builder webClientBuilder;

    public ServiceNowServiceImpl(WebClient.Builder webClientBuilder) {
        log.info("Loading ServiceNowServiceImpl!!!!");
        this.webClientBuilder = webClientBuilder.filter(logRequest());
    }

    @Override
    public void getRITMDetailsByRITMNumber(String ritmNumber) {
        Flux<Object> objectFlux = webClientBuilder.build().get().uri(serviceNowRITMUrl).exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Object.class));
        objectFlux.collectList().block().forEach(obj -> System.out.println(obj.toString()));
    }

    private ExchangeFilterFunction logRequest() {
        return ((clientRequest, exchangeFunction) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return exchangeFunction.exchange(clientRequest);
        });
    }
}
