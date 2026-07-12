package edu.OrbitaMarket.OrdersService;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "completed")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPaymentCompletedTransactionalInbox {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id")
    private String userId;

    @Column
    private Long amount;

    @Column(name = "new_balance")
    private Long newBalance;
}
