package edu.OrbitaMarket.APIGateway;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(Properties.class)
@RequiredArgsConstructor
public class RestClientConfig {
    private final Properties properties;

    @Bean
    public RestClient ordersClient() {
        return RestClient.builder()
        .baseUrl(properties.ordersserviceurl())
        .build();
    }

    @Bean
    public RestClient paymentsClient() {
        return RestClient.builder()
        .baseUrl(properties.paymentsserviceurl())
        .build();
    }
}
