package edu.OrbitaMarket.OrdersService;

public class UnknownProductTypeException extends RuntimeException {
    UnknownProductTypeException(String message) {
        super(message);
    }
}
