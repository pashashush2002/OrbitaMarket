package edu.OrbitaMarket.PaymentsService;

import java.util.UUID;

public record RequestedEvent(
    UUID event_id,
    Long orderId,
    String userId,
    Long amount
) {
}
