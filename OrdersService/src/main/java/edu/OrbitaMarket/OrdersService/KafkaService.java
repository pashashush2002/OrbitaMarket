package edu.OrbitaMarket.OrdersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaService {
    private final KafkaTemplate<String, RequestedEvent> kafkaTemplate;

    public void sendToKafkaSatellite(String topic, RequestedEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.event_id()), event);
        log.info("Отправлено событие в kafka: " + event);
    }
}
