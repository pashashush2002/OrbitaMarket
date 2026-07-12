package edu.OrbitaMarket.PaymentsService;

import java.util.UUID;

public record CompletedEvent(
    UUID event_id,
    Long orderId,
    String userId,
    Long amount,
    Long newBalance) {
}
