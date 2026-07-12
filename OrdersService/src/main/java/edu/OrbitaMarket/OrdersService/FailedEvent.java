package edu.OrbitaMarket.OrdersService;

import java.util.UUID;

public record FailedEvent(
    UUID event_id,
    Long orderId,
    String userId,
    String reason) {
}
