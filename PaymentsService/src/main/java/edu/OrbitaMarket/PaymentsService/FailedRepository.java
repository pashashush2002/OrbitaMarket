package edu.OrbitaMarket.PaymentsService;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FailedRepository extends JpaRepository<OrderPaymentFailedTransactionalOutbox, UUID> {
    @Query("SELECT o FROM OrderPaymentFailedTransactionalOutbox o WHERE o.status = :status ORDER BY o.createdAt ASC")
    List<OrderPaymentFailedTransactionalOutbox> findByStatusOrderByCreatedByAsc(@Param("status") OrderPaymentFailedTransactionalOutbox.OutboxStatus status, Pageable page);

    @Modifying
    @Query("UPDATE OrderPaymentFailedTransactionalOutbox o SET o.status = :status WHERE o.id = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") OrderPaymentFailedTransactionalOutbox.OutboxStatus status);
}
