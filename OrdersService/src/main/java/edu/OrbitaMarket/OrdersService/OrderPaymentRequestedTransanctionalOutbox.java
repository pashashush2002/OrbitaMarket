package edu.OrbitaMarket.OrdersService;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "requested")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPaymentRequestedTransanctionalOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    private UUID id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private String userId;

    @Column
    private Long amount;

    private String payload;

    @Column(name = "occuried_at")
    private LocalDateTime occuriedAt;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    public enum OutboxStatus { PENDING, SENT, FAILED }
}
