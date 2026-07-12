package edu.OrbitaMarket.OrdersService;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedRepository extends JpaRepository<OrderPaymentCompletedTransactionalInbox, UUID> {
    
}
