package edu.OrbitaMarket.PaymentsService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestedRepository extends JpaRepository<OrderPaymentRequestedTransanctionalInbox, UUID> {
    @Modifying
    @Query(value = """
        INSERT INTO requested (event_id, order_id, user_id, amount, occuried_at) 
        VALUES (:id, :orderId, :userId, :amount, :occuriedAt) 
        ON CONFLICT (order_id) DO NOTHING
    """, nativeQuery = true)
    int saveIfNotExistNative(
        @Param("id") UUID id, 
        @Param("orderId") Long orderId, 
        @Param("userId") String userId, 
        @Param("amount") Long amount, 
        @Param("occuriedAt") LocalDateTime occuriedAt
    );
}
