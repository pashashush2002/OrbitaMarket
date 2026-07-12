package edu.OrbitaMarket.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Component
public class InboxListener {
    private final RequestedRepository repository;
    private final AccountService accountService;
    private final OutboxService outboxService;

    @KafkaListener(topics = "requested", groupId = "requested", containerFactory = "kafkaFactory")
    @Transactional
    public void handleEvent(ConsumerRecord<String, RequestedEvent> record) {
        try {
            RequestedEvent event = record.value();
            log.info("Получено сообщение: " + event);
            int rows = repository.saveIfNotExistNative(
                event.event_id(),
                event.orderId(),
                event.userId(),
                event.amount(),
            LocalDateTime.now());
            if (rows == 0) {
                log.info("Событие уже обработано, пропускаем");
                return;
            }
        try {
            accountService.expense(event.userId(), event.amount());
        }
        catch (AccountNotFoundException e) {
            outboxService.publishFailedToOutbox(new FailedEvent(
                UUID.randomUUID(),
                event.orderId(),
                event.userId(),
            e.getMessage()));
        }
        catch (InsufficientBalanceException e) {
            outboxService.publishFailedToOutbox(new FailedEvent(
                UUID.randomUUID(),
                event.orderId(),
                event.userId(),
            e.getMessage()));
        }
        outboxService.publishCompletedToOutbox(
            new CompletedEvent(
                UUID.randomUUID(),
                event.orderId(),
                event.userId(),
                event.amount(),
            accountService.getBalance(event.userId()))
        );
            
        }
        catch (Exception e) {
            log.error("Произошло исключение: " + e.getMessage());
        }
    }

}
