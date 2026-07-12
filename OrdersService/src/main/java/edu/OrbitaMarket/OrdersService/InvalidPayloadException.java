package edu.OrbitaMarket.OrdersService;

public class InvalidPayloadException extends RuntimeException {
    InvalidPayloadException(String message) {
        super(message);
    }
}
