package edu.OrbitaMarket.PaymentsService;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompletedRepository extends JpaRepository<OrderPaymentCompletedTransactionalOutbox, UUID> {
    @Query("SELECT o FROM OrderPaymentCompletedTransactionalOutbox o WHERE o.status = :status ORDER BY o.createdAt ASC")
    List<OrderPaymentCompletedTransactionalOutbox> findByStatusOrderByCreatedByAsc(@Param("status") OrderPaymentCompletedTransactionalOutbox.OutboxStatus status, Pageable page);

    @Modifying
    @Query("UPDATE OrderPaymentCompletedTransactionalOutbox o SET o.status = :status WHERE o.id = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") OrderPaymentCompletedTransactionalOutbox.OutboxStatus status);
}
