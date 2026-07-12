package edu.OrbitaMarket.OrdersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import edu.OrbitaMarket.OrdersService.OrderPaymentRequestedTransanctionalOutbox.OutboxStatus;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxEventService {
    private final KafkaService kafkaService;
    private final RequestedRepository repository;
    private final ObjectMapper objectMapper;

    private static final String topic = "requested";
    private static final int batch_size = 50;

    public void publishToOutbox(RequestedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            OrderPaymentRequestedTransanctionalOutbox outboxEvent = OrderPaymentRequestedTransanctionalOutbox.builder()
            .orderId(event.orderId())
            .userId(event.userId())
            .amount(event.amount())
            .payload(payload)
            .occuriedAt(LocalDateTime.now())
            .status(OutboxStatus.PENDING)
            .build();
            repository.save(outboxEvent);
        }
        catch (JacksonException e) {
            String error = "Ошибка сериализации JSON: " + e.getMessage();
            log.error(error);
            throw new RuntimeException(error);
        }
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {
        List<OrderPaymentRequestedTransanctionalOutbox> pending = repository.findByStatusOrderByCreatedByAsc(
                OrderPaymentRequestedTransanctionalOutbox.OutboxStatus.PENDING,
                PageRequest.of(0, batch_size));
        if(pending.isEmpty()) {
            return;
        }
        for (OrderPaymentRequestedTransanctionalOutbox event: pending) {
            try {
                RequestedEvent requestedEvent = objectMapper.readValue(event.getPayload(), RequestedEvent.class);
                kafkaService.sendToKafkaSatellite(
                        topic,
                        requestedEvent
                );
                repository.updateStatus(event.getId(), OrderPaymentRequestedTransanctionalOutbox.OutboxStatus.SENT);
            }
            catch (JacksonException e) {
                String error = "Ошибка десериализации: " + e.getMessage();
                log.error(error);
                repository.updateStatus(event.getId(), OrderPaymentRequestedTransanctionalOutbox.OutboxStatus.FAILED);
                throw new RuntimeException(error);
            }
        }
    }
}