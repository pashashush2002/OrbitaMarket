package edu.OrbitaMarket.OrdersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class InboxListener {
    private final FailedRepository failedRepository;
    private final CompletedRepository completedRepository;
    private final ProductRepository productRepository;

    @KafkaListener(topics = "completed", groupId = "completed", containerFactory = "kafkaCompletedFactory")
    public void handleCompletedEvent(ConsumerRecord<String, CompletedEvent> record) {
        try {
            CompletedEvent event = record.value();
            log.info("Получено сообщение: " + event);

            OrderPaymentCompletedTransactionalInbox completedEvent = 
            OrderPaymentCompletedTransactionalInbox.builder()
            .id(event.event_id())
            .orderId(event.orderId())
            .userId(event.userId())
            .amount(event.amount())
            .newBalance(event.newBalance())
            .build();


            if (completedRepository.existsById(completedEvent.getId())) {
                log.info("Событие уже обработано, пропускаем");
                return;
            }

            Product product = productRepository.findById(completedEvent.getOrderId()).orElseThrow();

            product.setStatus(Status.PAID);
            productRepository.save(product);
        }
        catch (Exception e) {
            log.error("Произошло исключение: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "failed", groupId = "failed", containerFactory = "kafkaFailedFactory")
    public void handleFailedEvent(ConsumerRecord<String, FailedEvent> record) {
        try {
            FailedEvent event = record.value();
            log.info("Получено сообщение: " + event);

            OrderPaymentFailedTransactionalInbox failedEvent =
            OrderPaymentFailedTransactionalInbox.builder()
            .id(event.event_id())
            .orderId(event.orderId())
            .userId(event.userId())
            .reason(event.reason())
            .build();

            if (failedRepository.existsById(failedEvent.getId())) {
                log.info("Событие уже обработано, пропускаем");
                return;
            }

            Product product = productRepository.findById(failedEvent.getOrderId()).orElseThrow();

            product.setStatus(Status.PAYMENT_FAILED);
            product.setFailureReason(failedEvent.getReason());
            productRepository.save(product);
        }
        catch (Exception e) {
            log.error("Произошло исключение: " + e.getMessage());
        }
    }
}
