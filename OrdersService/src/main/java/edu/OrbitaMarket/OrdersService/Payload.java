package edu.OrbitaMarket.OrdersService;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Payload {
    @Getter
    private final String aoi; // принимаем как есть, обработка в других модулях
    @Getter
    private final LocalDateTime captureDate;
    @Getter
    private final String sensorType;
    @Getter
    private final MonitoringProduct.Cadence cadence;
    @Getter
    private final Long durationDays;
    @Getter
    private final TaskingProduct.TimeWindow timeWindow;
}
