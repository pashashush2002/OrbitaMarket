package edu.OrbitaMarket.APIGateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.apigateway")
public record Properties(String ordersserviceurl, String paymentsserviceurl) {
}
