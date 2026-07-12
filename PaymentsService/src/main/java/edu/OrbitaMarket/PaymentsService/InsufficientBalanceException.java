package edu.OrbitaMarket.PaymentsService;

public class InsufficientBalanceException extends RuntimeException {
    InsufficientBalanceException(String message) {
        super(message);
    }
}
