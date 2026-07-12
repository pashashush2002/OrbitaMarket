package edu.OrbitaMarket.OrdersService;

public class InvalidPriceException extends RuntimeException {
    InvalidPriceException(String message) {
        super(message);
    }
}
