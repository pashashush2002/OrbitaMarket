package edu.OrbitaMarket.PaymentsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxService {
    private final KafkaService<CompletedEvent> kafkaCompletedService;
    private final KafkaService<FailedEvent> kafkaFailedService;
    private final CompletedRepository completedRepository;
    private final FailedRepository failedRepository;
    private final ObjectMapper objectMapper;

    private static final String completedTopic = "completed";
    private static final String failedTopic = "failed";
    private static final int batch_size = 50;

    public void publishCompletedToOutbox(CompletedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OrderPaymentCompletedTransactionalOutbox outboxEvent = OrderPaymentCompletedTransactionalOutbox.builder()
            .orderId(event.orderId())
            .userId(event.userId())
            .amount(event.amount())
            .newBalance(event.newBalance())
            .createdAt(LocalDateTime.now())
            .payload(payload)
            .status(OrderPaymentCompletedTransactionalOutbox.OutboxStatus.PENDING)
            .build();
            completedRepository.save(outboxEvent);
        }
        catch (JacksonException e) {
            String error = "Ошибка сериализации JSON: " + e.getMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    public void publishFailedToOutbox(FailedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OrderPaymentFailedTransactionalOutbox outboxEvent = OrderPaymentFailedTransactionalOutbox.builder()
            .orderId(event.orderId())
            .userId(event.userId())
            .reason(event.reason())
            .payload(payload)
            .createdAt(LocalDateTime.now())
            .status(OrderPaymentFailedTransactionalOutbox.OutboxStatus.PENDING)
            .build();
            failedRepository.save(outboxEvent);
        }
        catch (JacksonException e) {
            String error = "Ошибка сериализации JSON: " + e.getMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishCompletedPendingEvents() {
        List<OrderPaymentCompletedTransactionalOutbox> pending = completedRepository.findByStatusOrderByCreatedByAsc(
                OrderPaymentCompletedTransactionalOutbox.OutboxStatus.PENDING,
                PageRequest.of(0, batch_size));
        if(pending.isEmpty()) {
            return;
        }
        for (OrderPaymentCompletedTransactionalOutbox event: pending) {
            try {
                CompletedEvent completedEvent = objectMapper.readValue(event.getPayload(), CompletedEvent.class);
                kafkaCompletedService.sendToKafkaSatellite(
                        completedTopic,
                        event.getId(),
                        completedEvent
                );
                completedRepository.updateStatus(event.getId(), OrderPaymentCompletedTransactionalOutbox.OutboxStatus.SENT);
            }
            catch (JacksonException e) {
                String error = "Ошибка десериализации: " + e.getMessage();
                log.error(error);
                completedRepository.updateStatus(event.getId(), OrderPaymentCompletedTransactionalOutbox.OutboxStatus.FAILED);
                throw new RuntimeException(error);
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishFailedPendingEvents() {
        List<OrderPaymentFailedTransactionalOutbox> pending = failedRepository.findByStatusOrderByCreatedByAsc(
                OrderPaymentFailedTransactionalOutbox.OutboxStatus.PENDING,
                PageRequest.of(0, batch_size));
        if(pending.isEmpty()) {
            return;
        }
        for (OrderPaymentFailedTransactionalOutbox event: pending) {
            try {
                FailedEvent failedEvent = objectMapper.readValue(event.getPayload(), FailedEvent.class);
                kafkaFailedService.sendToKafkaSatellite(
                        failedTopic,
                        event.getId(),
                        failedEvent
                );
                failedRepository.updateStatus(event.getId(), OrderPaymentFailedTransactionalOutbox.OutboxStatus.SENT);
            }
            catch (JacksonException e) {
                String error = "Ошибка десериализации: " + e.getMessage();
                log.error(error);
                failedRepository.updateStatus(event.getId(), OrderPaymentFailedTransactionalOutbox.OutboxStatus.FAILED);
                throw new RuntimeException(error);
            }
        }
    }
}
