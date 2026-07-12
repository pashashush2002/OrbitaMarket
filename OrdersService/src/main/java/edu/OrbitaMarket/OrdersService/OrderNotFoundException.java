package edu.OrbitaMarket.OrdersService;

public class OrderNotFoundException extends RuntimeException {
    OrderNotFoundException(String message) {
        super(message);
    }
}
