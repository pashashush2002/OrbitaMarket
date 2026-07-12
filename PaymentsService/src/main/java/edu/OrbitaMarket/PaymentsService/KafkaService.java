package edu.OrbitaMarket.PaymentsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaService<T> {
    private final KafkaTemplate<String, T> kafkaTemplate;

    public void sendToKafkaSatellite(String topic, UUID key, T event) {
        kafkaTemplate.send(topic, String.valueOf(key), event);
        log.info("Отправлено событие в kafka: " + event);
    }
}
