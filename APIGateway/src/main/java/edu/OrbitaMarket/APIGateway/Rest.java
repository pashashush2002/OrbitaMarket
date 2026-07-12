package edu.OrbitaMarket.APIGateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Rest {
    private final RestClient ordersClient;
    private final RestClient paymentsClient;

    @PostMapping("/payments/accounts")
    public ResponseEntity<String> createAccount(@RequestHeader String userId) {
        return paymentsClient.post()
        .uri("/accounts")
        .header("userId", userId)
        .retrieve()
        .toEntity(String.class);
    }

    @PostMapping("/payments/accounts/top-up")
    public ResponseEntity<String> topUpAccount(@RequestHeader String userId, @RequestBody Long amount) {
        return paymentsClient.post()
        .uri("/accounts/top-up")
        .header("userId", userId)
        .body(amount)
        .retrieve()
        .toEntity(String.class);
    }
    
    @GetMapping("/payments/accounts/balance")
    public ResponseEntity<String> getBalance(@RequestHeader String userId) {
        return paymentsClient.get()
        .uri("/accounts/balance")
        .header("userId", userId)
        .retrieve()
        .toEntity(String.class);
    }

    @PostMapping("/orders/orders")
    public ResponseEntity<String> createOrder(@RequestHeader String userId, @RequestBody Order order) {
        return ordersClient.post()
        .uri("/orders")
        .header("userId", userId)
        .body(order)
        .retrieve()
        .toEntity(String.class);
    }

    @GetMapping("/orders/orders")
    public ResponseEntity<String> getOrders(@RequestHeader String userId) {
        return ordersClient.get()
        .uri("/orders")
        .header("userId", userId)
        .retrieve()
        .toEntity(String.class);
    }

    @GetMapping("/orders/orders/{order_id}")
    public ResponseEntity<String> getOrder(@RequestHeader String userId, @PathVariable Long order_id) {
        return ordersClient.get()
        .uri("/orders/{order_id}", order_id)
        .header("userId", userId)
        .retrieve()
        .toEntity(String.class);
    }
               
    
}
