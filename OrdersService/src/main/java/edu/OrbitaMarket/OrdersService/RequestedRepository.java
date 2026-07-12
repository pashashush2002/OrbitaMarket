package edu.OrbitaMarket.OrdersService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RequestedRepository extends JpaRepository<OrderPaymentRequestedTransanctionalOutbox, UUID> {
    @Query("SELECT o FROM OrderPaymentRequestedTransanctionalOutbox o WHERE o.status = :status ORDER BY o.occuriedAt ASC")
    List<OrderPaymentRequestedTransanctionalOutbox> findByStatusOrderByCreatedByAsc(@Param("status") OrderPaymentRequestedTransanctionalOutbox.OutboxStatus status, Pageable page);

    @Modifying
    @Query("UPDATE OrderPaymentRequestedTransanctionalOutbox o SET o.status = :status WHERE o.id = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") OrderPaymentRequestedTransanctionalOutbox.OutboxStatus status);
}
